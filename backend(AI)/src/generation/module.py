from fastapi import UploadFile, File, Form, Depends, FastAPI
from sqlalchemy.orm import Session
from config.module import get_db
from .schema import post_ai_image
from config.s3 import upload_to_s3
from config.models import Artwork

import functools
import tensorflow as tf
import tensorflow_hub as hub
import numpy as np
from PIL import Image
import os
import uuid
import requests
from io import BytesIO

def insert_post(post_ai : post_ai_image, db: Session):
    post = Artwork(
        artwork_type = post_ai.artwork_type,
        member_id = post_ai.member_id,
        ai_artwork_title = post_ai.ai_artwork_title,
        ai_artwork_img = post_ai.ai_img_url
    )

    db.add(post)
    db.commit()
    db.refresh(post)

    return {
        "artwork_id" : post.artwork_id
    }

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

@functools.lru_cache(maxsize=None)
def load_image_from_url(image_url, image_size=(256, 256), preserve_aspect_ratio=True):
    response = requests.get(image_url)
    image = Image.open(BytesIO(response.content))
    image = image.convert("RGB")
    image = image.resize(image_size)
    image_np = np.array(image) / 255.0
    image_np = np.expand_dims(image_np, axis=0)
    return tf.convert_to_tensor(image_np, dtype=tf.float32)

hub_module = None

# 애플리케이션 시작 시 모듈을 한 번만 로드
def load_hub_module():
    global hub_module
    hub_handle = 'https://tfhub.dev/google/magenta/arbitrary-image-stylization-v1-256/2'
    hub_module = hub.load(hub_handle)

# 이미지 스타일 전송 함수
def transfer_image(content_image: UploadFile = File(), style_image: int = Form(), db: Session = Depends(get_db)):
    global hub_module
    if hub_module is None:
        raise RuntimeError("The model has not been loaded.")

    style_image_path = db.query(Artwork).filter(Artwork.artwork_id == style_image).first().filename
    style_image_url = os.path.join("https://j11d106.p.ssafy.io/static", style_image_path).replace('\\', '/')

    temp_dir = 'backend(AI)/content_image'
    if not os.path.exists(temp_dir):
        os.makedirs(temp_dir)

    content_image_filename = f"{uuid.uuid4()}_{content_image.filename}"
    content_image_path = os.path.join(temp_dir, content_image_filename)
    
    # 업로드된 content 이미지를 temp 디렉토리에 저장
    with open(content_image_path, "wb") as f:
        f.write(content_image.file.read())

    content_image_size = (256, 256)
    content = load_image(content_image_path, content_image_size)

    style_image_size = (256, 256)
    style = load_image_from_url(style_image_url, style_image_size)
    style = tf.nn.avg_pool(style, ksize=[3, 3], strides=[1, 1], padding='SAME')

    # 스타일 변환 수행
    outputs = hub_module(tf.constant(content), tf.constant(style))
    stylized_image = outputs[0]

    # 텐서를 이미지로 변환
    stylized_image_np = stylized_image.numpy()
    stylized_image_np = np.squeeze(stylized_image_np, axis=0)
    stylized_image_np = (stylized_image_np * 255).astype(np.uint8)
    image_pil = Image.fromarray(stylized_image_np)

    img_byte_arr = BytesIO()
    image_pil.save(img_byte_arr, format='JPEG')
    img_byte_arr.seek(0)

    s3_filename = f"{uuid.uuid4()}.jpg"
    bucket_name = 'ssafy-arti'
    upload_to_s3(img_byte_arr, bucket_name, s3_filename)

    s3_url = f"https://{bucket_name}.s3.amazonaws.com/{s3_filename}"

    return {"image_url": s3_url}

