import sys
import os

# src 폴더를 Python 경로에 추가
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from fastapi import FastAPI, Depends, HTTPException
from fastapi.responses import FileResponse
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session

from recommend import urls as rec_url
from generation import urls as gen_url
from generation import test
from ar import urls as ar_url
from config.database import SessionLocal, engine, Base, SQLALCHEMY_DATABASE_URL
from config.models import Artist, Artwork

app = FastAPI()

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
app.include_router(test.router)
app.include_router(ar_url.router)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get('/{artwork_id}')
def get_test(artwork_id : int, db : Session = Depends(get_db)):
    try:
        results = db.query(Artwork).filter(Artwork.artwork_id == artwork_id).first()
        return {
            "result" : results,
            "path" :  '/home/ubuntu/artwork/' + results.filename
        }
    except Exception as e:
        return HTTPException(status_code=500, detail=str(e))

@app.get('/image/{artwork_id}')
def get_test(artwork_id : int, db : Session = Depends(get_db)):
    try:
        results = db.query(Artwork).filter(Artwork.artwork_id == artwork_id).first()
        image_path = os.path.join('/home/ubuntu/artwork', results.filename)
        # image_path = os.path.join('C:/Users/SSAFY/Desktop/wikiart', results.filename)
        if os.path.exists(image_path):
            return FileResponse(image_path, media_type='image/jpg')
        else:
            raise HTTPException(status_code=404, detail="Image not found")
    except Exception as e:
        return HTTPException(status_code=500, detail=str(e))
    
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", reload=True)