import requests
import json
import os
from io import BytesIO
import torch
import torch.nn.functional as F
from torchvision import transforms
from PIL import Image
import faiss
import numpy as np

import logging
import traceback
import random
import time

import aiohttp
import asyncio
from torch.cuda.amp import autocast

from fastapi import HTTPException
from typing import List
from sqlalchemy.orm import Session
from config.models import Gallery, Member, Theme, Artwork_Theme, Artwork, Cold_Start
from .schema import GalleryBase, Owner, ThemeBase, ArtworksBase
from .deepLearning import EfficientNetV2

model = None

all_artwork_vector = None

transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])

def load_model(path='path', device='gpu'):
    model = EfficientNetV2(num_classes=27)
    model.load_state_dict(torch.load(path, map_location=device, weights_only=True))
    model.to(device)
    model.eval()
    return model

def startup_recommend():
    global model, all_artwork_vector
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    model = load_model(r'src/recommend/best_model(EfficientNetV2).pth', device)
    npy_file_path = 'src/recommend/save_np.npy'
    all_artwork_vector = np.load(npy_file_path)

async def fetch_image(session, artwork, device):
    try:
        async with session.get(artwork) as response:
            if response.status == 200:
                img = Image.open(BytesIO(await response.read())).convert('RGB')
                return transform(img).unsqueeze(0).to(device)
            else:
                print(f"Failed to fetch image from {artwork}")
                return None
    except Exception as e:
        print(f"Error fetching image from {artwork}: {e}")
        return None

async def download_images(artworks, device):
    async with aiohttp.ClientSession() as session:
        tasks = [fetch_image(session, artwork, device) for artwork in artworks]
        return await asyncio.gather(*tasks)
    
def compute_gallery_vector_batch(artworks: List[str], model, device):
    try:
        imgs = asyncio.run(download_images(artworks, device))
        imgs = [img for img in imgs if img is not None]  

        if len(imgs) > 0:
            img_batch = torch.cat(imgs, dim=0) 
            with torch.no_grad():
                with autocast():  
                    vectors = model(img_batch).cpu().numpy() 
            return np.array(vectors)
        else:
            return None
    except Exception as e:
        print(f"Error in vector batch processing: {e}")
        raise HTTPException(status_code=500, detail=f"Vector batch processing error: {str(e)}")

def recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx, top_k=3):
    time.sleep(0.1)
    if user_gallery_vector.ndim == 1:
        user_gallery_vector = np.expand_dims(user_gallery_vector, axis=0)
        
    d = user_gallery_vector.shape[1]
    index = faiss.IndexFlatL2(d)
    index.add(all_gallery_vector)
    
    try:
        distances, indices = index.search(user_gallery_vector, top_k)
        # print(f"Search result - distances: {distances}, indices: {indices}")
    except Exception as e:
        print(f"Error in faiss search: {e}")
        raise

    return [gallery_idx[i] for i in indices[0]]

def custom_base(similar_galleries, db: Session):
    result = []

    # 추천된 미술관 처리
    for similar_gallery in similar_galleries:
        sim_gal = GalleryBase(
            gallery_id=similar_gallery.gallery_id,
            gallery_title=similar_gallery.gallery_title,
            gallery_desc=similar_gallery.gallery_desc,
            gallery_img=similar_gallery.gallery_img,
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

                if file.artwork_type == 'AI':
                    artwork_title = file.ai_artwork_title
                    artwork_url = file.ai_artwork_img
                else:
                    artwork_title = file.title
                    artwork_url = 'https://j11d106.p.ssafy.io/static/' + file.filename

                sim_artwork = ArtworksBase(
                    artwork_id=artwork.artwork_id,
                    title=artwork_title,
                    description=file.description,
                    year=file.year,
                    image_url=artwork_url
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

def recommend_gallery(user_id, db: Session):
    global model
    if model is None:
        raise RuntimeError("Model is not loaded. Please wait until the model is loaded.")

    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    # 자신을 제외한 미술관 정보 추출
    art_galleries = db.query(Gallery).filter(Gallery.owner_id != user_id).all()
    art_gallery_data = [gallery.gallery_img for gallery in art_galleries]
    gallery_idx = [gallery.gallery_id for gallery in art_galleries]

    # 사용자 미술관 정보 추출
    user_gallery = db.query(Gallery).filter(Gallery.owner_id == user_id).first()

    if not user_gallery:
        cold_start_artworks = db.query(Cold_Start).filter(Cold_Start.member_id == user_id).all()
        if not cold_start_artworks or len(cold_start_artworks) < 5:
            raise HTTPException(status_code=404, detail="Cold Start: Not enough artworks available for recommendation.")
        
        cold_start_artwork_ids = [cs.artwork_id for cs in random.sample(cold_start_artworks,3)]
        result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(cold_start_artwork_ids)).all()
        user_gallery_path = ['https://j11d106.p.ssafy.io/static/' + cs.filename for cs in result_artworks]
    else:
        user_gallery_path = [user_gallery.gallery_img]
        response = requests.get(f'https://j11d106.p.ssafy.io/members/{user_id}/instagram/media')
        if response.status_code == 200:
            instar = json.loads(str(response.content,'utf-8'))
            if len(instar):
                instar_random = random.sample(instar, 1)
                user_gallery_path.append(instar_random)
        
    user_gallery_vector = compute_gallery_vector_batch(user_gallery_path, model, device)

    if user_gallery_vector is None:
        raise ValueError("User gallery vector could not be computed.")
    
    all_gallery_vector = compute_gallery_vector_batch(art_gallery_data, model, device)

    if all_gallery_vector is None:
        raise ValueError("Other gallery vector could not be computed.")

    # 미술관 추천(6개)
    similar_gallery_ids = recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx, 50)

    recommed_idx = [idx for idx in random.sample(set(similar_gallery_ids), 6)]

    similar_galleries = db.query(Gallery).filter(Gallery.gallery_id.in_(recommed_idx)).all()

    result = custom_base(similar_galleries, db)

    return result

#------------------------------------미술품----------------------------------
def compute_artwork_vector_batch(artworks: List[str], model, device):
    vectors = []
    imgs = []
    for artwork in artworks:
        try:

            artwork_path = os.path.join("https://j11d106.p.ssafy.io/static", artwork).replace('\\', '/')
            response = requests.get(artwork_path)
            if response.status_code == 200:

                img = Image.open(BytesIO(response.content)).convert('RGB')
                imgs.append(transform(img).unsqueeze(0).to(device))
            else:
                print(f"Failed to fetch image from {artwork_path}")
        except Exception as e:
            print(f"Error processing {artwork_path}: {e}")
            continue

    if len(imgs) > 0:
        img_batch = torch.cat(imgs, dim=0)
        with torch.no_grad():
            embedding_vectors = model(img_batch).cpu().numpy()
        vectors = [embedding.flatten() for embedding in embedding_vectors]

    if vectors:
        mean_vector = np.mean(vectors, axis=0)
        return np.array(mean_vector)
    else:
        return None
    
def recommend_similar_artworks(user_artworks_vector, all_artworks_vector, artwork_idx, top_k=50):
    time.sleep(0.1)
    # 벡터 차원 확인
    logging.info(f"user_artworks_vector shape: {user_artworks_vector.shape}")
    logging.info(f"all_artworks_vector shape: {all_artworks_vector.shape}")
    
    # 여러 벡터의 평균을 사용하여 하나의 벡터로 요약
    if user_artworks_vector.shape[0] > 1:
        user_artworks_vector = np.mean(user_artworks_vector, axis=0)
        logging.info(f"Averaged user_artworks_vector shape: {user_artworks_vector.shape}")
    
    d = user_artworks_vector.shape[0]  # 벡터 차원 (27)

    user_artworks_vector = user_artworks_vector.astype(np.float32)
    all_artworks_vector = all_artworks_vector.astype(np.float32)

    index = faiss.IndexFlatL2(d)  # FAISS 인덱스 초기화
    
    index.add(all_artworks_vector)  # 전체 미술품 벡터 추가
    _, indices = index.search(user_artworks_vector.reshape(1, -1), top_k)  # 유사한 벡터 검색
    return [artwork_idx[i] for i in indices[0]]


def recommend_artwork(user_id, db: Session):
    try:
        device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
        logging.info(f"Device selected: {device}")
        
        # 사용자 갤러리 추출
        user_gallery = db.query(Gallery).filter(Gallery.owner_id == user_id).first()
        logging.info(f"User gallery: {user_gallery}")

        if not user_gallery:
            logging.info("No user gallery found, falling back to cold start.")
            cold_start_artworks = db.query(Cold_Start).filter(Cold_Start.member_id == user_id).all()
            if not cold_start_artworks:
                raise HTTPException(status_code=404, detail="No artworks available for recommendation.")
            
            cold_start_idx = [idx.artwork_id for idx in random.sample(cold_start_artworks, 3)]
            cold_start_idx = [i for i in cold_start_idx if i < all_artwork_vector.shape[0]]

            cold_start_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(cold_start_idx)).all()
            cold_start_artwork_paths = [f"https://j11d106.p.ssafy.io/static/{artwork.filename}" for artwork in cold_start_artworks]
            logging.info(f"Cold start artworks: {cold_start_artwork_paths}")
            
            cold_artwork_vector = compute_gallery_vector_batch(cold_start_artwork_paths, model, device)
            filtered_artwork_vector = np.delete(all_artwork_vector, cold_start_idx, axis=0)
            recommended_artworks = recommend_similar_artworks(cold_artwork_vector, filtered_artwork_vector, list(range(len(all_artwork_vector))), top_k=50)

            result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(recommended_artworks)).all()
            return [
                {"artwork_id": artwork.artwork_id, "title": artwork.title, "image_url": f"https://j11d106.p.ssafy.io/static/{artwork.filename}", "year" : artwork.year, "writer" : artwork.artist_name}
                for artwork in result_artworks]

        # 갤러리 테마 및 미술품 처리
        user_theme = db.query(Theme).filter(Theme.gallery_id == user_gallery.gallery_id).all()
        logging.info(f"User theme: {user_theme}")
        
        user_theme_ids = [theme.theme_id for theme in user_theme]
        user_artwork_ids = db.query(Artwork_Theme.artwork_id).filter(Artwork_Theme.theme_id.in_(user_theme_ids)).all()
        logging.info(f"User artwork ids: {user_artwork_ids}")

        if len(user_artwork_ids) < 5:
            logging.info("Not enough user artworks, falling back to cold start.")
            cold_start_artworks = db.query(Cold_Start).filter(Cold_Start.member_id == user_id).all()
            cold_start_idx = [idx.artwork_id for idx in random.sample(cold_start_artworks, 3)]
            cold_start_idx = [i for i in cold_start_idx if i < all_artwork_vector.shape[0]]

            cold_start_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(cold_start_idx)).all()
            cold_start_artwork_paths = [f"https://j11d106.p.ssafy.io/static/{artwork.filename}" for artwork in cold_start_artworks]
            
            cold_artwork_vector = compute_gallery_vector_batch(cold_start_artwork_paths, model, device)
            filtered_artwork_vector = np.delete(all_artwork_vector, cold_start_idx, axis=0)
            recommended_artworks = recommend_similar_artworks(cold_artwork_vector, filtered_artwork_vector, list(range(len(all_artwork_vector))), top_k=50)

            result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(recommended_artworks)).all()
            return [{"artwork_id": artwork.artwork_id, "title": artwork.title, "image_url": f"https://j11d106.p.ssafy.io/static/{artwork.filename}", "year" : artwork.year, "writer" : artwork.artist_name} for artwork in result_artworks]

        user_artwork_ids = [i.artwork_id for i in user_artwork_ids if i.artwork_id < all_artwork_vector.shape[0]]
        user_artwork_ids = [idx for idx in random.sample(user_artwork_ids, 3)]
        user_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(user_artwork_ids)).all()
        user_artwork_paths = [f"https://j11d106.p.ssafy.io/static/{artwork.filename}" for artwork in user_artworks]
        logging.info(f"User artwork paths: {user_artwork_paths}")
        
        user_artwork_vector = compute_gallery_vector_batch(user_artwork_paths, model, device)
        if user_artwork_vector is None:
            raise HTTPException(status_code=500, detail="Failed to compute artwork vector")

        # 올바른 인덱스를 사용하여 all_artwork_vector에서 제거
        filtered_artwork_vector = np.delete(all_artwork_vector, user_artwork_ids, axis=0)
        recommended_artworks = recommend_similar_artworks(user_artwork_vector, filtered_artwork_vector, list(range(len(all_artwork_vector))), top_k=50)

        result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(recommended_artworks)).all()
        return [{"artwork_id": artwork.artwork_id, "title": artwork.title, "image_url": f"https://j11d106.p.ssafy.io/static/{artwork.filename}", "year" : artwork.year, "writer" : artwork.artist_name} for artwork in result_artworks]

    except Exception as e:
        logging.error(f"Error in recommend_artwork: {str(e)}")
        logging.error(traceback.format_exc())  # 전체 스택 트레이스 기록
        raise HTTPException(status_code=500, detail=str(e))