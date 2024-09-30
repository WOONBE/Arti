import sys
import os
import logging
import time

# src 폴더를 Python 경로에 추가
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from fastapi import FastAPI, Depends, HTTPException, Request
from fastapi.responses import FileResponse
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from contextlib import asynccontextmanager

from generation.module import load_hub_module
from recommend.module import startup_recommend
from music.module import load_music_model
from recommend import urls as rec_url
from generation import urls as gen_url
from music import urls as music_url
from portfolio import urls as portfolio_url
from config.database import SessionLocal, engine, Base, SQLALCHEMY_DATABASE_URL
from config.models import Artist, Artwork
from config.module import get_db

@asynccontextmanager
async def lifespan(app: FastAPI):
    # When service starts.
    load_hub_module()
    startup_recommend()
    load_music_model()

    yield
    
    # When service is stopped.

app = FastAPI(lifespan=lifespan)

# Dependency
app.add_middleware(
    CORSMiddleware,
    allow_origins = [SQLALCHEMY_DATABASE_URL],
    allow_credentials = True,
    allow_methods = ["*"],
    allow_headers = ["*"]
)

app.include_router(rec_url.router)
app.include_router(gen_url.router)
app.include_router(portfolio_url.router)
app.include_router(music_url.router)

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@app.middleware("http")
async def log_process_time(request: Request, call_next):
    start_time = time.time()
    response = await call_next(request)
    process_time = time.time() - start_time
    logger.info(f"Request URL: {request.url}, Process time: {process_time}")
    return response

@app.get('/fastapi/artist/{artist_id}')
def get_artist(artist_id : int, db : Session = Depends(get_db)):
    result = db.query(Artist).filter(Artist.artist_id == artist_id).first()
    return result

@app.get('/fastapi/{artwork_id}')
def get_test(artwork_id : int, db : Session = Depends(get_db)):
    try:
        results = db.query(Artwork).filter(Artwork.artwork_id == artwork_id).first()
        return {
            "result" : results,
            "path" :  '/artwork/images/' + results.filename
        }
    except Exception as e:
        return HTTPException(status_code=500, detail=str(e))

@app.get('/fastapi/image/{artwork_id}')
def get_image(artwork_id: int, db: Session = Depends(get_db)):
    try:
        # DB에서 artwork_id에 해당하는 레코드 조회
        results = db.query(Artwork).filter(Artwork.artwork_id == artwork_id).first()

        if not results:
            raise HTTPException(status_code=404, detail="Artwork not found in the database")

        # 이미지 경로 설정 (EC2 절대 경로)
        # image_path = os.path.join('/home/ubuntu/artwork', results.filename)
        image_path = os.path.join('/artwork/images', results.filename)

        # 이미지 파일이 존재하는지 확인
        if os.path.exists(image_path):
            return FileResponse(image_path, media_type='image/jpg')
        else:
            raise HTTPException(status_code=404, detail="Image not found")

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
if __name__ == "__main__":
    import uvicorn
    import torch
    device = "cuda" if torch.cuda.is_available() else "cpu"
    print(f"Using {device} device")
    uvicorn.run("main:app", reload=True)