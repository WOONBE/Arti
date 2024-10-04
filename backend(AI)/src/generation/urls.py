from fastapi import APIRouter, HTTPException, Depends, UploadFile, File, Form
from fastapi.responses import FileResponse
from config.database import SessionLocal
from config.module import get_db
from sqlalchemy.orm import Session
from .schema import post_ai_image
from .module import insert_post, transfer_image
import os

router = APIRouter(prefix='/artwork')


@router.post('/ai', tags=['generation'])
def generation_image(content_image: UploadFile = File(), style_image : int = Form(), db : Session = Depends(get_db)):
    
    image_path = transfer_image(content_image, style_image, db)
    
    return image_path
    # if os.path.exists(image_path):
    #     return FileResponse(image_path, media_type='image/jpg')
    #     # return {
    #     #     'image_url': image_path
    #     # }
    # else:
    #     raise HTTPException(status_code=400, detail="Image not found")

@router.post('/ai/save',tags=['generation'])
def get_image(image: post_ai_image, db: Session = Depends(get_db)):

    return insert_post(image, db)