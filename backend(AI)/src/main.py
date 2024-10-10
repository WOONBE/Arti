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
from cold_start import urls as cold_start_url
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
app.include_router(cold_start_url.router)

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@app.middleware("http")
async def log_process_time(request: Request, call_next):
    start_time = time.time()
    response = await call_next(request)
    process_time = time.time() - start_time
    logger.info(f"Request URL: {request.url}, Process time: {process_time}")
    return response
    
if __name__ == "__main__":
    import uvicorn
    import torch
    import nest_asyncio
    from pyngrok import ngrok
    import gc
    import json

    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    SECRET_FILE = os.path.join(BASE_DIR, 'ngrok.json')
    secrets = json.loads(open(SECRET_FILE).read())
    ngrok_token = secrets["ngrok"]

    # gc.disable()
    gc.enable()

    device = "cuda" if torch.cuda.is_available() else "cpu"
    print(f"Using {device} device")
    ngrok.set_auth_token(ngrok_token.get("token"))

    # ngrok_tunnel = ngrok.connect(8000)
    public_url = ngrok.connect(addr="8000", domain="just-shiner-manually.ngrok-free.app")
    print("Public URL:", public_url)
    # print('공용 URL:', ngrok_tunnel.public_url)
    nest_asyncio.apply()
    uvicorn.run("main:app")