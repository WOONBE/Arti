from fastapi import APIRouter, HTTPException, Response
from fastapi.responses import FileResponse
import functools
import tensorflow as tf
import tensorflow_hub as hub
import numpy as np
from PIL import Image
import io
import os
import uuid

router = APIRouter(prefix='/artwork')

def crop_center(image):
    shape = image.shape
    new_shape = min(shape[1], shape[2])
    offset_y = max(shape[1] - shape[2], 0) // 2
    offset_x = max(shape[2] - shape[1], 0) // 2
    image = tf.image.crop_to_bounding_box(
        image, offset_y, offset_x, new_shape, new_shape
    )
    return image

@functools.lru_cache(maxsize=None)
def load_image(image_path, image_size = (256,256), preserve_aspect_ratio=True):
    img = tf.io.decode_image(
        tf.io.read_file(image_path),
        channels=3, dtype=tf.float32
    )[tf.newaxis, ...]
    img = crop_center(img)
    img = tf.image.resize(img, image_size, preserve_aspect_ratio=True)
    return img

@router.post('/ai')
def generation_image(content_image_path, style_image_path):
    content_image_size = (256,256)
    content = load_image(content_image_path, content_image_size)

    style_image_size = (256,256)
    style = load_image(style_image_path, style_image_size)
    style = tf.nn.avg_pool(style, ksize=[3,3], strides=[1,1], padding='SAME')

    hub_handle = 'https://tfhub.dev/google/magenta/arbitrary-image-stylization-v1-256/2'
    hub_module = hub.load(hub_handle)

    outputs = hub_module(tf.constant(content), tf.constant(style))
    stylized_image = outputs[0]

    # return {
    #     'generation_image' : stylized_image.numpy().tolist()
    # }
    # 텐서를 이미지로 변환
    stylized_image_np = stylized_image.numpy()
    stylized_image_np = np.squeeze(stylized_image_np, axis=0)
    stylized_image_np = (stylized_image_np * 255).astype(np.uint8)
    image_pil = Image.fromarray(stylized_image_np)

    # 이미지를 서버의 로컬 경로에 저장
    save_dir = 'generated_images'
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    image_filename = f'{uuid.uuid4()}.png'
    image_path = os.path.join(save_dir, image_filename)
    image_pil.save(image_path)

    image_path = os.path.join('generated_images', image_filename)
    if os.path.exists(image_path):
        return FileResponse(image_path, media_type='image/png')
    else:
        raise HTTPException(status_code=404, detail="Image not found")
    
    # 이미지가 저장된 경로를 반환
    # image_url = f'/artwork/images/{image_filename}'
    # return {
    #     'image_url': image_url
    # }

@router.get('/images/{image_filename}')
def get_image(image_filename: str):
    image_path = os.path.join('generated_images', image_filename)
    if os.path.exists(image_path):
        return FileResponse(image_path, media_type='image/png')
    else:
        raise HTTPException(status_code=404, detail="Image not found")