from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from config.module import get_db
from .module import generation_music, delete_music_db, get_music

router = APIRouter(prefix='/music')

@router.post('{gallery_id}')
def music(gallery_id : int, db : Session = Depends(get_db)):
    result = generation_music(gallery_id, db)
    return result

@router.get('{gallery_id}')
def listen_music(gallery_id : int, db : Session = Depends(get_db)):
    result = get_music(gallery_id, db)
    return result

@router.delete('{gallery_id}')
def delete_music(gallert_id : int, db : Session = Depends(get_db)):
    result = delete_music_db(gallert_id, db)
    return result
