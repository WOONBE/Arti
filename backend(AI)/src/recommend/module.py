# import torch
# import torch.nn as nn
# import torch.nn.functional as F
# from torchvision import transforms

# from PIL import Image
# import faiss

# import numpy as np
# from typing import List
# import os

# from sqlalchemy.orm import Session
# from config.models import Gallery, Member, Theme, Artwork_Theme, Artwork
# from .schema import GalleryBase, Owner, ThemeBase, ArtworksBase

# os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"

# class SEBlock(nn.Module):
#     def __init__(self, in_channels, reduce_ratio = 4):
#         super(SEBlock, self).__init__()
#         reduced_channels = in_channels // reduce_ratio
#         self.fc1 = nn.Conv2d(in_channels, reduced_channels, kernel_size=1)
#         self.fc2 = nn.Conv2d(reduced_channels, in_channels, kernel_size=1)

#     def forward(self, x):
#         se = F.adaptive_avg_pool2d(x, 1)
#         se = torch.relu(self.fc1(se))
#         se = torch.sigmoid(self.fc2(se))
#         return x * se
    
# class MBConv(nn.Module):
#     def __init__(self, in_channels, out_channels, expansion, stride, se_ratio = 0.25):
#         super(MBConv, self).__init__()
#         self.use_residual = stride == 1 and in_channels == out_channels
#         mid_channels = in_channels * expansion

#         self.expand_conv = nn.Conv2d(in_channels, mid_channels, kernel_size=1, bias=False)
#         self.bn1 = nn.BatchNorm2d(mid_channels)
#         self.deptwise_conv = nn.Conv2d(mid_channels, mid_channels, kernel_size=3, stride = stride, padding=1, groups=mid_channels, bias=False)
#         self.bn2 = nn.BatchNorm2d(mid_channels)
#         self.se = SEBlock(mid_channels, reduce_ratio = int(1 / se_ratio))
#         self.project_conv = nn.Conv2d(mid_channels, out_channels, kernel_size=1, bias=False)
#         self.bn3 = nn.BatchNorm2d(out_channels)
#         self.act = nn.SiLU()

#     def forward(self, x):
#         residual = x
#         x = self.act(self.bn1(self.expand_conv(x)))
#         x = self.act(self.bn2(self.deptwise_conv(x)))
#         x = self.se(x)
#         x = self.bn3(self.project_conv(x))
#         if self.use_residual:
#             x += residual
#         return x
    
# class FusedMBConv(nn.Module):
#     def __init__(self, in_channels, out_channels, expansion, stride):
#         super(FusedMBConv, self).__init__()
#         self.use_residual = stride == 1 and in_channels == out_channels
#         mid_channels = in_channels * expansion

#         self.expand_conv = nn.Conv2d(in_channels, mid_channels, kernel_size=3, stride=stride, padding=1, bias=False) if expansion != 1 else None
#         self.bn1 = nn.BatchNorm2d(mid_channels if expansion != 1 else in_channels)
#         self.project_conv = nn.Conv2d(mid_channels if expansion != 1 else in_channels, out_channels, kernel_size=1 if expansion != 1 else 3, stride=1, padding = 1 if expansion == 1 else 0, bias=False)
#         self.bn2 = nn.BatchNorm2d(out_channels)
#         self.act = nn.SiLU()

#     def forward(self, x):
#         residual = x
#         if self.expand_conv:
#             x = self.act(self.bn1(self.expand_conv(x)))
#         else:
#             x = self.act(self.bn1(x))
#         x = self.bn2(self.project_conv(x))
#         if self.use_residual:
#             x += residual
#         return x


# class EfficientNetV2(nn.Module):
#     def __init__(self, num_classes=27):
#         super(EfficientNetV2, self).__init__()

#         self.stem = nn.Sequential(
#             nn.Conv2d(3, 24, kernel_size=3, stride=2, padding=1, bias=False),
#             nn.BatchNorm2d(24),
#             nn.SiLU()
#         )
#         # (block type, repeats, in_channels, out_channels, expansion, stride)
#         self.block_config = [
#             (FusedMBConv, 2, 24, 24, 1, 1),
#             (FusedMBConv, 4, 24, 48, 4, 2),
#             (FusedMBConv, 4, 48, 64, 4, 2),
#             (MBConv, 6, 64, 128, 4, 2),
#             (MBConv, 9, 128, 160, 6, 1),
#             (MBConv, 15, 160, 256, 6, 2)
#         ]

#         layers = []
#         for block, repeats, in_channels, out_channels, expansion, stride in self.block_config:
#             for i in range(repeats):
#                 if i == 0:
#                     layers.append(block(in_channels, out_channels, expansion, stride))
#                 else:
#                     layers.append(block(out_channels, out_channels, expansion, 1))
#         self.blocks = nn.Sequential(*layers)

#         self.head = nn.Sequential(
#         nn.Conv2d(256,1280, kernel_size=1, bias=False),
#         nn.BatchNorm2d(1280),
#         nn.SiLU(),
#         nn.AdaptiveAvgPool2d(1),
#         nn.Flatten(),
#         nn.Linear(1280, num_classes)
#         )

#     def forward(self, x):
#         x = self.stem(x)
#         x = self.blocks(x)
#         x = self.head(x)
#         return x
        

# def load_model(path='path', device='cpu'):
#     model = EfficientNetV2(num_classes = 27)
#     model.load_state_dict(torch.load(path, map_location=device, weights_only=True))
#     model.eval()
#     return model


# def compute_gallery_vector(artwork, model, device):
#     vectors = []

#     vector = image_to_embedding(artwork, model, device)
#     if vector is not None:
#             vectors.append(vector)

#     if vectors:
#         return np.mean(vectors, axis=0)
#     else:
#         return None

# def compute_gallery_vector_batch(artworks: List[str], model, device):
#     vectors = []
#     transform = transforms.Compose([
#         transforms.Resize((224, 224)),
#         transforms.ToTensor(),
#         transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
#     ])

#     imgs = []
#     for artwork in artworks:
#         artwork_path = os.path.join("C:/Users/SSAFY/Desktop/wikiart", artwork)
#         if not os.path.exists(artwork_path):
#             continue
#         img = Image.open(artwork_path).convert('RGB')
#         imgs.append(transform(img).unsqueeze(0).to(device))

#     if len(imgs) > 0:
#         img_batch = torch.cat(imgs, dim=0)  # 배치 처리
#         with torch.no_grad():
#             embedding_vectors = model(img_batch).cpu().numpy()
#         vectors = [embedding.flatten() for embedding in embedding_vectors]

#     if vectors:
#         return np.mean(vectors, axis=0)
#     else:
#         return None
        
# def image_to_embedding(artwork, model, device):
#     artwork_path = os.path.join("C:/Users/SSAFY/Desktop/wikiart", artwork)
#     # artwork_path = os.path.join("/artwork/images", artwork)
#     if not os.path.exists(artwork_path):
#         return None
    
#     transform = transforms.Compose([
#         transforms.Resize((224,224)),
#         transforms.ToTensor(),
#         transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
#     ])

#     img = Image.open(artwork_path).convert('RGB')
#     img_tensor = transform(img).unsqueeze(0).to(device)

#     with torch.no_grad():
#         embedding_vector = model(img_tensor).cpu().numpy()

#     return embedding_vector.flatten()

# def recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx, top_k=3):
#     d = user_gallery_vector.shape[0]
#     index = faiss.IndexFlatL2(d)
#     index.add(all_gallery_vector)
#     _, indices = index.search(np.array([user_gallery_vector]), top_k)
#     return [gallery_idx[i] for i in indices[0]]

# def custom_base(top_view_galleries, similar_galleries, db:Session):
#     result = []

#     # 최다 조회수 미술관 처리
#     for top_gallery in top_view_galleries:
#         top_gal = GalleryBase(
#             gallery_id = top_gallery.gallery_id,
#             gallery_title = top_gallery.gallery_title,
#             gallery_desc = top_gallery.gallery_desc,
#             gallery_img = 'https://j11d106.p.ssafy.io/static/' + top_gallery.gallery_img,
#             gallery_view = top_gallery.gallery_view
#         )
#         top_user_base = db.query(Member).filter(Member.member_id == top_gallery.owner_id).first()
#         top_user = Owner(
#             member_id = top_user_base.member_id,
#             email = top_user_base.email,
#             nickname = top_user_base.nickname
#         )
#         top_theme = []
#         top_theme_base = db.query(Theme).filter(Theme.gallery_id == top_gallery.gallery_id).all()
#         for top_theme_filter in top_theme_base:
#             top_artworks = db.query(Artwork_Theme).filter(Artwork_Theme.theme_id == top_theme_filter.theme_id).all()
#             artwork_list = []
#             for artwork in top_artworks:
#                 file = db.query(Artwork).filter(Artwork.artwork_id == artwork.artwork_id).first()
#                 top_artwork = ArtworksBase(
#                     artwork_id = artwork.artwork_id,
#                     image_url = 'https://j11d106.p.ssafy.io/static/' + file.filename
#                 )
#                 artwork_list.append(top_artwork)
#             theme_base = ThemeBase(
#                 theme_id = top_theme_filter.theme_id,
#                 theme_name = top_theme_filter.theme_name,
#                 artworks = artwork_list
#             )
#             top_theme.append(theme_base)

#         result.append({
#             'gallery' : top_gal,
#             'user' : top_user,
#             'theme' : top_theme
#         })
#     # 추천된 미술관 처리
#     for similar_gallery in similar_galleries:
#         sim_gal = GalleryBase(
#             gallery_id=similar_gallery.gallery_id,
#             gallery_title=similar_gallery.gallery_title,
#             gallery_desc=similar_gallery.gallery_desc,
#             gallery_img='https://j11d106.p.ssafy.io/static/' + similar_gallery.gallery_img,
#             gallery_view=similar_gallery.gallery_view
#         )
#         sim_user_base = db.query(Member).filter(Member.member_id == similar_gallery.owner_id).first()
#         sim_user = Owner(
#             member_id=sim_user_base.member_id,
#             email=sim_user_base.email,
#             nickname=sim_user_base.nickname
#         )
#         sim_theme = []
#         sim_theme_base = db.query(Theme).filter(Theme.gallery_id == similar_gallery.gallery_id).all()
#         for sim_theme_filter in sim_theme_base:
#             sim_artworks = db.query(Artwork_Theme).filter(Artwork_Theme.theme_id == sim_theme_filter.theme_id).all()
#             artwork_list = []
#             for artwork in sim_artworks:
#                 file = db.query(Artwork).filter(Artwork.artwork_id == artwork.artwork_id).first()
#                 sim_artwork = ArtworksBase(
#                     artwork_id=artwork.artwork_id,
#                     image_url='https://j11d106.p.ssafy.io/static/' + file.filename
#                 )
#                 artwork_list.append(sim_artwork)
#             theme_base = ThemeBase(
#                 theme_id=sim_theme_filter.theme_id,
#                 theme_name=sim_theme_filter.theme_name,
#                 artworks=artwork_list
#             )
#             sim_theme.append(theme_base)

#         result.append({
#             'gallery': sim_gal,
#             'user': sim_user,
#             'theme': sim_theme
#         })

#     return result
    
# def recommend(user_id , db: Session):
#     device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
#     model = load_model(r'./src/recommend/best_model(EfficientNetV2).pth', device)

#     # 일주일간 최다 조회수 미술관 추출(3개)
#     top_view_galleries = db.query(Gallery).order_by(Gallery.gallery_view.desc()).limit(3).all()

#     top_gallery_ids = {gallery.gallery_id for gallery in top_view_galleries}
    
#     # 자신을 제외한 미술관 정보 추출
#     art_galleries = db.query(Gallery).filter(Gallery.owner_id != user_id).all()
#     art_gallery_data = [gallery.gallery_img for gallery in art_galleries if gallery.gallery_id not in top_gallery_ids]
#     gallery_idx = [gallery.gallery_id for gallery in art_galleries if gallery.gallery_id not in top_gallery_ids]

#     # 사용자 미술관 정보 추출
#     user_gallery = db.query(Gallery).filter(Gallery.owner_id == user_id).first()
#     user_gallery_path = user_gallery.gallery_img
#     user_gallery_vector = compute_gallery_vector(user_gallery_path, model, device)

#     all_gallery_vector = []
    
#     idx = 0
#     for art_gallery_image in art_gallery_data:
#         gallery_vector = compute_gallery_vector(art_gallery_image, model, device)
#         if gallery_vector is not None:
#             all_gallery_vector.append(gallery_vector)
#         idx += 1

#     if len(all_gallery_vector) > 0:
#         all_gallery_vector = np.array(all_gallery_vector)
#     else:
#         all_gallery_vector = np.array([np.zeros_like(user_gallery_vector)])

#     # # 미술관 추천(3개)
#     similar_gallery_ids  = recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx)

#     simmilar_galleries = db.query(Gallery).filter(Gallery.gallery_id.in_(similar_gallery_ids)).all()

#     result = custom_base(top_view_galleries, simmilar_galleries, db)

#     return result



import torch
import torch.nn as nn
import torch.nn.functional as F
from torchvision import transforms
from PIL import Image
import faiss
import numpy as np
import os
from concurrent.futures import ThreadPoolExecutor
from typing import List
from sqlalchemy.orm import Session
from config.models import Gallery, Member, Theme, Artwork_Theme, Artwork
from .schema import GalleryBase, Owner, ThemeBase, ArtworksBase

os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"


class SEBlock(nn.Module):
    def __init__(self, in_channels, reduce_ratio=4):
        super(SEBlock, self).__init__()
        reduced_channels = in_channels // reduce_ratio
        self.fc1 = nn.Conv2d(in_channels, reduced_channels, kernel_size=1)
        self.fc2 = nn.Conv2d(reduced_channels, in_channels, kernel_size=1)

    def forward(self, x):
        se = F.adaptive_avg_pool2d(x, 1)
        se = torch.relu(self.fc1(se))
        se = torch.sigmoid(self.fc2(se))
        return x * se


class MBConv(nn.Module):
    def __init__(self, in_channels, out_channels, expansion, stride, se_ratio=0.25):
        super(MBConv, self).__init__()
        self.use_residual = stride == 1 and in_channels == out_channels
        mid_channels = in_channels * expansion

        self.expand_conv = nn.Conv2d(in_channels, mid_channels, kernel_size=1, bias=False)
        self.bn1 = nn.BatchNorm2d(mid_channels)
        self.deptwise_conv = nn.Conv2d(mid_channels, mid_channels, kernel_size=3, stride=stride, padding=1, groups=mid_channels, bias=False)
        self.bn2 = nn.BatchNorm2d(mid_channels)
        self.se = SEBlock(mid_channels, reduce_ratio=int(1 / se_ratio))
        self.project_conv = nn.Conv2d(mid_channels, out_channels, kernel_size=1, bias=False)
        self.bn3 = nn.BatchNorm2d(out_channels)
        self.act = nn.SiLU()

    def forward(self, x):
        residual = x
        x = self.act(self.bn1(self.expand_conv(x)))
        x = self.act(self.bn2(self.deptwise_conv(x)))
        x = self.se(x)
        x = self.bn3(self.project_conv(x))
        if self.use_residual:
            x += residual
        return x


class FusedMBConv(nn.Module):
    def __init__(self, in_channels, out_channels, expansion, stride):
        super(FusedMBConv, self).__init__()
        self.use_residual = stride == 1 and in_channels == out_channels
        mid_channels = in_channels * expansion

        self.expand_conv = nn.Conv2d(in_channels, mid_channels, kernel_size=3, stride=stride, padding=1, bias=False) if expansion != 1 else None
        self.bn1 = nn.BatchNorm2d(mid_channels if expansion != 1 else in_channels)
        self.project_conv = nn.Conv2d(mid_channels if expansion != 1 else in_channels, out_channels, kernel_size=1 if expansion != 1 else 3, stride=1, padding=1 if expansion == 1 else 0, bias=False)
        self.bn2 = nn.BatchNorm2d(out_channels)
        self.act = nn.SiLU()

    def forward(self, x):
        residual = x
        if self.expand_conv:
            x = self.act(self.bn1(self.expand_conv(x)))
        else:
            x = self.act(self.bn1(x))
        x = self.bn2(self.project_conv(x))
        if self.use_residual:
            x += residual
        return x


class EfficientNetV2(nn.Module):
    def __init__(self, num_classes=27):
        super(EfficientNetV2, self).__init__()

        self.stem = nn.Sequential(
            nn.Conv2d(3, 24, kernel_size=3, stride=2, padding=1, bias=False),
            nn.BatchNorm2d(24),
            nn.SiLU()
        )
        self.block_config = [
            (FusedMBConv, 2, 24, 24, 1, 1),
            (FusedMBConv, 4, 24, 48, 4, 2),
            (FusedMBConv, 4, 48, 64, 4, 2),
            (MBConv, 6, 64, 128, 4, 2),
            (MBConv, 9, 128, 160, 6, 1),
            (MBConv, 15, 160, 256, 6, 2)
        ]

        layers = []
        for block, repeats, in_channels, out_channels, expansion, stride in self.block_config:
            for i in range(repeats):
                if i == 0:
                    layers.append(block(in_channels, out_channels, expansion, stride))
                else:
                    layers.append(block(out_channels, out_channels, expansion, 1))
        self.blocks = nn.Sequential(*layers)

        self.head = nn.Sequential(
            nn.Conv2d(256, 1280, kernel_size=1, bias=False),
            nn.BatchNorm2d(1280),
            nn.SiLU(),
            nn.AdaptiveAvgPool2d(1),
            nn.Flatten(),
            nn.Linear(1280, num_classes)
        )

    def forward(self, x):
        x = self.stem(x)
        x = self.blocks(x)
        x = self.head(x)
        return x


def load_model(path='path', device='cpu'):
    model = EfficientNetV2(num_classes=27)
    model.load_state_dict(torch.load(path, map_location=device, weights_only=True))
    model.eval()
    return model


def compute_gallery_vector_batch(artworks: List[str], model, device):
    vectors = []
    transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])

    imgs = []
    for artwork in artworks:
        # artwork_path = os.path.join("C:/Users/SSAFY/Desktop/wikiart", artwork)
        artwork_path = os.path.join("/artwork/images", artwork)
        if not os.path.exists(artwork_path):
            continue
        img = Image.open(artwork_path).convert('RGB')
        imgs.append(transform(img).unsqueeze(0).to(device))

    if len(imgs) > 0:
        img_batch = torch.cat(imgs, dim=0)
        with torch.no_grad():
            embedding_vectors = model(img_batch).cpu().numpy()
        vectors = [embedding.flatten() for embedding in embedding_vectors]

    if vectors:
        return np.array(vectors)
    else:
        return None


def recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx, top_k=3):
    d = user_gallery_vector.shape[1]  # Update to handle batches
    index = faiss.IndexFlatL2(d)
    index.add(all_gallery_vector)
    _, indices = index.search(user_gallery_vector, top_k)
    return [gallery_idx[i] for i in indices[0]]

def custom_base(top_view_galleries, similar_galleries, db: Session):
    result = []

    # 최다 조회수 미술관 처리
    for top_gallery in top_view_galleries:
        top_gal = GalleryBase(
            gallery_id=top_gallery.gallery_id,
            gallery_title=top_gallery.gallery_title,
            gallery_desc=top_gallery.gallery_desc,
            gallery_img='https://j11d106.p.ssafy.io/static/' + top_gallery.gallery_img,
            gallery_view=top_gallery.gallery_view
        )
        top_user_base = db.query(Member).filter(Member.member_id == top_gallery.owner_id).first()
        top_user = Owner(
            member_id=top_user_base.member_id,
            email=top_user_base.email,
            nickname=top_user_base.nickname
        )
        top_theme = []
        top_theme_base = db.query(Theme).filter(Theme.gallery_id == top_gallery.gallery_id).all()
        for top_theme_filter in top_theme_base:
            top_artworks = db.query(Artwork_Theme).filter(Artwork_Theme.theme_id == top_theme_filter.theme_id).all()
            artwork_list = []
            for artwork in top_artworks:
                file = db.query(Artwork).filter(Artwork.artwork_id == artwork.artwork_id).first()
                top_artwork = ArtworksBase(
                    artwork_id=artwork.artwork_id,
                    image_url='https://j11d106.p.ssafy.io/static/' + file.filename
                )
                artwork_list.append(top_artwork)
            theme_base = ThemeBase(
                theme_id=top_theme_filter.theme_id,
                theme_name=top_theme_filter.theme_name,
                artworks=artwork_list
            )
            top_theme.append(theme_base)

        result.append({
            'gallery': top_gal,
            'user': top_user,
            'theme': top_theme
        })

    # 추천된 미술관 처리
    for similar_gallery in similar_galleries:
        sim_gal = GalleryBase(
            gallery_id=similar_gallery.gallery_id,
            gallery_title=similar_gallery.gallery_title,
            gallery_desc=similar_gallery.gallery_desc,
            gallery_img='https://j11d106.p.ssafy.io/static/' + similar_gallery.gallery_img,
            gallery_view=similar_gallery.gallery_view
        )
        sim_user_base = db.query(Member).filter(Member.member_id == similar_gallery.owner_id).first()
        sim_user = Owner(
            member_id=sim_user_base.member_id,
            email=sim_user_base.email,
            nickname=sim_user_base.nickname
        )
        sim_theme = []
        sim_theme_base = db.query(Theme).filter(Theme.gallery_id == similar_gallery.gallery_id).all()
        for sim_theme_filter in sim_theme_base:
            sim_artworks = db.query(Artwork_Theme).filter(Artwork_Theme.theme_id == sim_theme_filter.theme_id).all()
            artwork_list = []
            for artwork in sim_artworks:
                file = db.query(Artwork).filter(Artwork.artwork_id == artwork.artwork_id).first()
                sim_artwork = ArtworksBase(
                    artwork_id=artwork.artwork_id,
                    image_url='https://j11d106.p.ssafy.io/static/' + file.filename
                )
                artwork_list.append(sim_artwork)
            theme_base = ThemeBase(
                theme_id=sim_theme_filter.theme_id,
                theme_name=sim_theme_filter.theme_name,
                artworks=artwork_list
            )
            sim_theme.append(theme_base)

        result.append({
            'gallery': sim_gal,
            'user': sim_user,
            'theme': sim_theme
        })

    return result


def recommend(user_id, db: Session):
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    # model = load_model(r'/src/recommend/best_model(EfficientNetV2).pth', device)
    model = load_model(r'src/recommend/best_model(EfficientNetV2).pth', device)

    # 일주일간 최다 조회수 미술관 추출(3개)
    top_view_galleries = db.query(Gallery).order_by(Gallery.gallery_view.desc()).limit(3).all()

    top_gallery_ids = {gallery.gallery_id for gallery in top_view_galleries}

    # 자신을 제외한 미술관 정보 추출
    art_galleries = db.query(Gallery).filter(Gallery.owner_id != user_id).all()
    art_gallery_data = [gallery.gallery_img for gallery in art_galleries if gallery.gallery_id not in top_gallery_ids]
    gallery_idx = [gallery.gallery_id for gallery in art_galleries if gallery.gallery_id not in top_gallery_ids]

    # 사용자 미술관 정보 추출
    user_gallery = db.query(Gallery).filter(Gallery.owner_id == user_id).first()
    user_gallery_path = user_gallery.gallery_img

    user_gallery_vector = compute_gallery_vector_batch([user_gallery_path], model, device)

    if user_gallery_vector is None:
        raise ValueError("User gallery vector could not be computed.")
    
    all_gallery_vector = compute_gallery_vector_batch(art_gallery_data, model, device)

    if all_gallery_vector is None:
        all_gallery_vector = np.array([np.zeros_like(user_gallery_vector)])

    # 미술관 추천(3개)
    similar_gallery_ids = recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx)

    similar_galleries = db.query(Gallery).filter(Gallery.gallery_id.in_(similar_gallery_ids)).all()

    result = custom_base(top_view_galleries, similar_galleries, db)

    return result


