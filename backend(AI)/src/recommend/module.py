import requests
import os
from io import BytesIO
import torch
import torch.nn.functional as F
from torchvision import transforms
from PIL import Image
import faiss
import numpy as np

import logging

from fastapi import HTTPException
from typing import List
from sqlalchemy.orm import Session
from config.models import Gallery, Member, Theme, Artwork_Theme, Artwork, Cold_Start
from .schema import GalleryBase, Owner, ThemeBase, ArtworksBase
from .deepLearning import EfficientNetV2

model = None

all_artwork_vector = None

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

def compute_gallery_vector_batch(artworks: List[str], model, device):
    vectors = []
    transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])

    imgs = []
    for artwork in artworks:
        try:
            response = requests.get(artwork)
            if response.status_code == 200:
                img = Image.open(BytesIO(response.content)).convert('RGB')
                imgs.append(transform(img).unsqueeze(0).to(device))
            else:
                print(f"Failed to fetch image from {artwork}")
        except Exception as e:
            print(f"Error processing {artwork}: {e}")
            continue

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
    if user_gallery_vector.ndim == 1:
        user_gallery_vector = np.expand_dims(user_gallery_vector, axis=0)
        
    d = user_gallery_vector.shape[1]
    index = faiss.IndexFlatL2(d)
    index.add(all_gallery_vector)
    
    try:
        distances, indices = index.search(user_gallery_vector, top_k)
        print(f"Search result - distances: {distances}, indices: {indices}")
    except Exception as e:
        print(f"Error in faiss search: {e}")
        raise

    return [gallery_idx[i] for i in indices[0]]

def custom_base(top_view_galleries, similar_galleries, db: Session):
    result = []

    # 최다 조회수 미술관 처리
    for top_gallery in top_view_galleries:
        top_gal = GalleryBase(
            gallery_id=top_gallery.gallery_id,
            gallery_title=top_gallery.gallery_title,
            gallery_desc=top_gallery.gallery_desc,
            gallery_img=top_gallery.gallery_img,
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
                
                if file.artwork_type == 'AI':
                    artwork_title = file.ai_artwork_title
                    artwork_url = file.ai_artwork_img
                else:
                    artwork_title = file.title
                    artwork_url = 'https://j11d106.p.ssafy.io/static/' + file.filename

                top_artwork = ArtworksBase(
                    artwork_id=artwork.artwork_id,
                    title=artwork_title,
                    description=file.description,
                    year=file.year,
                    image_url=artwork_url
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

    # 일주일간 최다 조회수 미술관 추출(3개)
    top_view_galleries = db.query(Gallery).order_by(Gallery.gallery_view.desc()).limit(3).all()

    top_gallery_ids = {gallery.gallery_id for gallery in top_view_galleries}

    # 자신을 제외한 미술관 정보 추출
    art_galleries = db.query(Gallery).filter(Gallery.owner_id != user_id).all()
    art_gallery_data = [gallery.gallery_img for gallery in art_galleries if gallery.gallery_id not in top_gallery_ids]
    gallery_idx = [gallery.gallery_id for gallery in art_galleries if gallery.gallery_id not in top_gallery_ids]

    # 사용자 미술관 정보 추출
    user_gallery = db.query(Gallery).filter(Gallery.owner_id == user_id).first()
    if not user_gallery:
        cold_start_artworks = db.query(Cold_Start).filter(Cold_Start.member_id == user_id).limit(5).all()
        if not cold_start_artworks or len(cold_start_artworks) < 5:
            raise HTTPException(status_code=404, detail="Cold Start: Not enough artworks available for recommendation.")
        
        cold_start_artwork_ids = [cs.artwork_id for cs in cold_start_artworks]
        result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(cold_start_artwork_ids)).all()
        user_gallery_path = [cs.filename for cs in result_artworks]
    else:
        user_gallery_path = list(user_gallery.gallery_img)

    user_gallery_vector = compute_gallery_vector_batch(user_gallery_path, model, device)

    if user_gallery_vector is None:
        raise ValueError("User gallery vector could not be computed.")
    
    all_gallery_vector = compute_gallery_vector_batch(art_gallery_data, model, device)

    if all_gallery_vector is None:
        raise ValueError("Other gallery vector could not be computed.")

    # 미술관 추천(3개)
    similar_gallery_ids = recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx)

    similar_galleries = db.query(Gallery).filter(Gallery.gallery_id.in_(similar_gallery_ids)).all()

    result = custom_base(top_view_galleries, similar_galleries, db)

    return result

#------------------------------------미술품----------------------------------
def compute_artwork_vector_batch(artworks: List[str], model, device):
    vectors = []
    transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])

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
    if user_artworks_vector.ndim == 1:
        user_artworks_vector = np.expand_dims(user_artworks_vector, axis=0)
        
    d = user_artworks_vector.shape[1]
    index = faiss.IndexFlatL2(d)
    index.add(all_artworks_vector)
    _, indices = index.search(user_artworks_vector, top_k)
    return [artwork_idx[i] for i in indices[0]]

def recommend_artwork(user_id, db : Session):
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    user_gallery = db.query(Gallery).filter(Gallery.owner_id == user_id).first()

    if not user_gallery:
        cold_start_artworks = db.query(Cold_Start).filter(Cold_Start.member_id == user_id).limit(5).all()
        if not cold_start_artworks or len(cold_start_artworks) < 5:
            raise HTTPException(status_code=404, detail="Cold Start: Not enough artworks available for recommendation.")
        
        cold_start_artwork_ids = [cs.artwork_id for cs in cold_start_artworks]
        result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(cold_start_artwork_ids)).all()
        cold_start_artwork_path = [cs.filename for cs in result_artworks]

        cold_artworks_vector = compute_artwork_vector_batch(cold_start_artwork_path, model, device)

        filtered_artwork_vector = np.delete(all_artwork_vector, cold_start_artwork_ids, axis=0)

        recommended_artworks = recommend_similar_artworks(cold_artworks_vector, filtered_artwork_vector, list(range(len(all_artwork_vector))), top_k=50)

        result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(recommended_artworks)).all()

        result = []
        for artwork in result_artworks:
            base = ArtworksBase(
                artwork_id=artwork.artwork_id,
                title=artwork.title,
                description=artwork.description,
                year=artwork.year,
                image_url='https://j11d106.p.ssafy.io/static/' + artwork.filename
            )
            result.append(base)

        return result

    user_theme = db.query(Theme).filter(Theme.gallery_id.in_([user_gallery.gallery_id])).all()
    user_theme_idx = [theme.theme_id for theme in user_theme]

    user_artworks_path = db.query(Artwork_Theme).filter(Artwork_Theme.theme_id.in_(user_theme_idx)).all()
    user_artwork_idx = [artwork.artwork_id for artwork in user_artworks_path]

    if len(user_artwork_idx) < 5:
        cold_start_artworks = db.query(Cold_Start).filter(Cold_Start.member_id == user_id).limit(5).all()
        if not cold_start_artworks or len(cold_start_artworks) < 5:
            raise HTTPException(status_code=404, detail="Cold Start: Not enough artworks available for recommendation.")
        
        cold_start_artwork_ids = [cs.artwork_id for cs in cold_start_artworks]
        result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(cold_start_artwork_ids)).all()
        cold_start_artwork_path = [cs.filename for cs in result_artworks]

        cold_artworks_vector = compute_artwork_vector_batch(cold_start_artwork_path, model, device)

        filtered_artwork_vector = np.delete(all_artwork_vector, cold_start_artwork_ids, axis=0)

        recommended_artworks = recommend_similar_artworks(cold_artworks_vector, filtered_artwork_vector, list(range(len(all_artwork_vector))), top_k=50)

        result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(recommended_artworks)).all()

        result = []
        for artwork in result_artworks:
            base = ArtworksBase(
                artwork_id=artwork.artwork_id,
                title=artwork.title,
                description=artwork.description,
                year=artwork.year,
                image_url='https://j11d106.p.ssafy.io/static/' + artwork.filename
            )
            result.append(base)

        return result

    user_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(user_artwork_idx)).limit(5)
    user_artworks_path = [artwork.filename for artwork in user_artworks]

    user_artworks_vector = compute_artwork_vector_batch(user_artworks_path, model, device)

    if user_artworks_vector is None:
        raise HTTPException(status_code=500, detail="Failed to compute gallery vector")

    filtered_artwork_vector = np.delete(all_artwork_vector, user_artwork_idx, axis=0)

    recommended_artworks = recommend_similar_artworks(user_artworks_vector, filtered_artwork_vector, list(range(len(all_artwork_vector))), top_k=50)
    
    result_artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(recommended_artworks)).all()
    result = []

    for artwork in result_artworks:
        base = ArtworksBase(
            artwork_id=artwork.artwork_id,
            title = artwork.title,
            description = artwork.description,
            year = artwork.year,
            image_url='https://j11d106.p.ssafy.io/static/' + artwork.filename
        )
        result.append(base)
    return result
