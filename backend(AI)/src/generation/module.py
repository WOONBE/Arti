from fastapi import UploadFile, File, Form, Depends
from sqlalchemy.orm import Session
from config.module import get_db
from .schema import post_ai_image
from config.models import Artwork

import functools
import tensorflow as tf
import tensorflow_hub as hub
import numpy as np
from PIL import Image
import os
import uuid

def insert_post(post_ai : post_ai_image, db: Session):
    post = Artwork(
        member_id = post_ai.member_id,
        ai_artwork_title = post_ai.ai_artwork_title,
        ai_img_url = post_ai.ai_img_url.split('/')[4],
        is_deleted = post_ai.is_deleted
    )

    db.add(post)
    db.commit()
    db.refresh(post)

    return post

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

def transfer_image(content_image:UploadFile = File(), style_image : int = Form(), db: Session  = Depends(get_db)):
    style_image_path = db.query(Artwork).filter(Artwork.artwork_id == style_image).first().filename
    # style_image_path = os.path.join("C:/Users/SSAFY/Desktop/wikiart", style_image_path)
    style_image_path = os.path.join("/artwork/images", style_image_path)

    # temp_dir = 'backend(AI)/content_image'
    temp_dir = '/artwork/images/content_image'
    if not os.path.exists(temp_dir):
        os.makedirs(temp_dir)  # 디렉토리 생성

    content_image_filename = f"{uuid.uuid4()}_{content_image.filename}"
    content_image_path = os.path.join(temp_dir, content_image_filename)
    
    # 업로드된 content 이미지를 temp 디렉토리에 저장
    with open(content_image_path, "wb") as f:
        f.write(content_image.file.read())

    content_image_size = (256,256)
    content = load_image(content_image_path, content_image_size)

    style_image_size = (256,256)
    style = load_image(style_image_path, style_image_size)
    style = tf.nn.avg_pool(style, ksize=[3,3], strides=[1,1], padding='SAME')

    hub_handle = 'https://tfhub.dev/google/magenta/arbitrary-image-stylization-v1-256/2'
    hub_module = hub.load(hub_handle)

    outputs = hub_module(tf.constant(content), tf.constant(style))
    stylized_image = outputs[0]

    # 텐서를 이미지로 변환
    stylized_image_np = stylized_image.numpy()
    stylized_image_np = np.squeeze(stylized_image_np, axis=0)
    stylized_image_np = (stylized_image_np * 255).astype(np.uint8)
    image_pil = Image.fromarray(stylized_image_np)

    # save_dir = 'backend(AI)/generated_images'
    save_dir = '/artwork/images/generated_images'
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    image_filename = f'{uuid.uuid4()}.jpg'
    image_path = os.path.join(save_dir, image_filename)
    image_pil.save(image_path)

    image_path = os.path.join('https://j11d106.p.ssafy.io/static/generated_images', image_filename)

    return image_path