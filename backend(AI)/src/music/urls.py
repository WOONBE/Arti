from fastapi import APIRouter, Depends
from fastapi.responses import FileResponse
from sqlalchemy.orm import Session
from config.module import get_db
from .module import generation_music, delete_music_db

router = APIRouter(prefix='/music')

@router.post('{gallery_id}')
def music(gallery_id : int, db : Session = Depends(get_db)):
    result,audio_file_name = generation_music(gallery_id, db)
    return FileResponse(path=result, media_type='audio/mpeg', filename=audio_file_name)

@router.delete('{gallery_id}')
def delete_music(gallert_id : int, db : Session = Depends(get_db)):
    result = delete_music_db(gallert_id, db)
    return result
