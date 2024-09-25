from fastapi import APIRouter, HTTPException, Depends, UploadFile, File, Form
from fastapi.responses import FileResponse
from config.database import SessionLocal
from config.models import AI_Artwork, Artwork
from config.module import get_db
from sqlalchemy.orm import Session
from .schema import post_ai_image, trasform_image
from .module import insert_post, transfer_image
import os

router = APIRouter(prefix='/fastapi/artwork')


@router.post('/ai')
def generation_image(content_image: UploadFile = File(), style_image : int = Form(), db : Session = Depends(get_db)):
    
    image_path = transfer_image(content_image, style_image, db)
    
    if os.path.exists(image_path):
        return {
            'image_url': image_path
        }
    else:
        raise HTTPException(status_code=400, detail="Image not found")

@router.post('/ai/save')
def get_image(image: post_ai_image, db: Session = Depends(get_db)):

    return insert_post(image, db)

@router.get('/ai/show')
def image_show(image_path : str):

    # 이미지 파일이 존재하는지 확인
    if os.path.exists(image_path):
        return FileResponse(image_path, media_type='image/jpg')
    else:
        raise HTTPException(status_code=404, detail="Image not found")