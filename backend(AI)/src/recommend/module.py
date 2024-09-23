import torch
import torch.nn
from PIL import Image
import faiss
from torchvision import transforms
import numpy as np
from typing import List
import os

from fastapi import Depends
from sqlalchemy.orm import Session
from config.module import get_db
from config.models import Gallery

def load_model(path='path'):
    model = torch.load(path)
    model.eval()
    return model


def compute_gallery_vector(gallery_artwork: List[str], model, device):
    vectors = []
    for artwork in gallery_artwork:
        vector = image_to_embedding(artwork, model, device)
        if vector is not None:
            vectors.append(vector)
    if vectors:
        return np.mean(vectors, axis=0)
    else:
        return None


def image_to_embedding(artwork, model, device):
    if not os.path.exists(artwork):
        return None
    
    transform = transforms.Compose([
        transforms.Resize((224,224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])

    img = Image.open(artwork).convert('RGB')
    img_tensor = transform(img).unsqueeze(0).to(device)

    with torch.no_grad():
        embedding_vector = model(img_tensor).cpu().numpy()

    return embedding_vector.flatten()

def recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx, top_k=3):
    d = user_gallery_vector.shape[0]
    index = faiss.IndexFlatL2(d)
    index.add(all_gallery_vector)
    _, indices = index.search(np.array([user_gallery_vector]), top_k)
    return [gallery_idx[i] for i in indices[0]]


def recommend(user_id : int, db: Session):
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    # model = load_model(r'best_model(EfficientNetV2).pth')

    art_gallery_data = []

    # 일주일간 최다 조회수 미술관 추출(3개)
    top_view_galleries = db.query(Gallery).order_by(Gallery.gallery_view.desc()).limit(3).all()

    simmilar_galleries = []

    # 사용자 미술관 정보 추출
    user_gallery = []
    # user_gallery_vector = compute_gallery_vector(user_gallery, model, device)

    # all_gallery_vector = []
    # gallery_idx = []
    # for gallery_id in gallery_idx:
    #     gallery_artworks = art_gallery_data[art_gallery_data['gallery_id'] == gallery_id]['artwork_path'].tolist()
    #     gallery_vector = compute_gallery_vector(gallery_artworks, model, device)
    #     if gallery_vector is not None:
    #         all_gallery_vector.append(gallery_vector)

    # all_gallery_vector = np.array(all_gallery_vector)

    # # 미술관 추천(3개)
    # simmilar_galleries = recommend_similar_galleries(user_gallery_vector, all_gallery_vector, gallery_idx)

    return simmilar_galleries, top_view_galleries