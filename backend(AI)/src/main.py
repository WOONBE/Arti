from fastapi import FastAPI, Depends
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session

from recommend import urls as rec_url
from generation import urls as gen_url
from generation import test
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

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get('/')
def get_test(db : Session = Depends(get_db)):
    results = db.query(Artist).offset(0).limit(100).all()
    return results

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", reload=True)