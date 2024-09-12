from fastapi import APIRouter, HTTPException, UploadFile, File
from fastapi.responses import FileResponse
import uuid
import os

import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim

from PIL import Image
import torchvision.transforms as transforms
from torchvision.models import vgg19, VGG19_Weights

import copy


router = APIRouter(prefix="/test")

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

def image_loader(image_file):
    image = Image.open(image_file)
    transform = transforms.Compose([
        transforms.Resize((224,224)),
        transforms.ToTensor()
    ])

    img_tensor = transform(image).unsqueeze(0).to(device)

    return img_tensor.to(device, torch.float)

def gram_matrix(input):
    a, b, c, d = input.size()

    features = input.view(a * b, c * d)
    G = torch.mm(features, features.t())

    return G.div(a * b * c * d)

class ContentLoss(nn.Module):
    
    def __init__(self, target):
        super(ContentLoss, self).__init__()
        self.target = target.detach()

    def forward(self, input):
        self.loss = F.mse_loss(input, self.target)
        return input
    
class StyleLoss(nn.Module):

    def __init__(self, target_feature):
        super(StyleLoss, self).__init__()
        self.target = gram_matrix(target_feature).detach()

    def forward(self, input):
        G = gram_matrix(input)
        self.loss = F.mse_loss(G, self.target)
        return input

cnn_normalization_mean = torch.tensor([0.485, 0.456, 0.406]).to(device)
cnn_normalization_std = torch.tensor([0.229, 0.244, 0.225]).to(device)

class Normalization(nn.Module):
    def __init__(self, mean, std):
        super(Normalization, self).__init__()
        self.mean = torch.tensor(mean).view(-1,1,1).to(device)
        self.std = torch.tensor(std).view(-1,1,1).to(device)

    def forward(self, img):
        return (img - self.mean) / self.std
    
cnn = vgg19(weights = VGG19_Weights.DEFAULT).features.eval().to(device)

content_layer_default = ['conv_4']
style_layer_default = ['conv_1','conv_2', 'conv_3', 'conv_4', 'conv_5']

def get_style_model_and_losses(cnn, normalization_mean, normalization_std ,style_img, content_img, content_layers = content_layer_default, style_layers = style_layer_default):

    normalization = Normalization(normalization_mean, normalization_std)

    content_losses = []
    style_losses = []

    model = nn.Sequential(normalization)

    i = 0
    for layer in cnn.children():
        if isinstance(layer, nn.Conv2d):
            i += 1
            name = 'conv_{}'.format(i)
        elif isinstance(layer, nn.ReLU):
            i += 1
            name = 'relu_{}'.format(i)
        elif isinstance(layer, nn.MaxPool2d):
            i += 1
            name = 'pool_{}'.format(i)
        elif isinstance(layer, nn.BatchNorm2d):
            i += 1
            name = 'batch_{}'.format(i)
        else:
            raise RuntimeError('Unrecognized layer : {}'.format(layer.__class__.__name__))
        
        model.add_module(name, layer)

        if name in content_layers:
            target = model(content_img).detach()
            content_loss = ContentLoss(target)
            model.add_module('content_loss_{}'.format(i), content_loss)
            content_losses.append(content_loss)

        if name in style_layers:
            target_feature = model(style_img).detach()
            style_loss = StyleLoss(target_feature)
            model.add_module('style_loss_{}'.format(i), style_loss)
            style_losses.append(style_loss)

    for i in range(len(model) -1, -1, -1):
        if isinstance(model[i], ContentLoss) or isinstance(model[i], StyleLoss):
            break

    model = model[:(i+1)]
    return model, style_losses, content_losses

def get_input_optimizer(input_img):
    optimizer = optim.LBFGS([input_img])
    return optimizer

def run_style_transfer(cnn, normalization_mean, normalization_std, content_img, style_img,
                       num_step=300, style_weight=1000000, content_weight=1):
    
    model, style_losses, content_losses = get_style_model_and_losses(cnn, normalization_mean, normalization_std, style_img, content_img)
    input_img = content_img.clone()

    input_img.requires_grad_(True)
    model.eval()
    model.requires_grad_(False)

    optimizer = get_input_optimizer(input_img)

    run = [0]
    while run[0] <= num_step:

        def closure():
            # inplace 연산을 피하기 위해 클램프 연산을 비인플레이스로 변경
            with torch.no_grad():
                input_img = input_img.clamp(0, 1)  # 기존에 사용한 inplace 연산을 변경함
            optimizer.zero_grad()
            model(input_img)
            style_score = 0
            content_score = 0

            for sl in style_losses:
                style_score += sl.loss
            
            for cl in content_losses:
                content_score += cl.loss

            style_score *= style_weight
            content_score *= content_weight

            loss = style_score + content_score
            loss.backward()

            run[0] += 1
            if run[0] % 50 == 0:
                print("run {}:".format(run))
                print('Style Loss : {:4f} Content Loss: {:4f}'.format(
                    style_score.item(), content_score.item()))
                print()
            return style_score + content_score
        
        optimizer.step(closure)

    # 마지막으로, 최종 이미지를 다시 클램핑할 때도 inplace가 아닌 방식 사용
    with torch.no_grad():
        input_img = input_img.clamp(0, 1)

    return input_img


@router.post('/ai')
def generation_image(content_image_path, style_image_path):

    content_img = image_loader(content_image_path)
    style_img = image_loader(style_image_path)

    output = run_style_transfer(cnn, cnn_normalization_mean, cnn_normalization_std, content_img, style_img)

    unloader = transforms.ToPILImage()
    output = output.cpu().clone()
    output = output.squeeze(0)
    output = unloader(output)

    # 이미지를 서버의 로컬 경로에 저장
    save_dir = 'generated_images'
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    image_filename = f'{uuid.uuid4()}.png'
    image_path = os.path.join(save_dir, image_filename)
    output.save(image_path)

    if os.path.exists(image_path):
        return FileResponse(image_path, media_type='image/png')
    else:
        raise HTTPException(status_code=404, detail="Image not found")
