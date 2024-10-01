import os
from fastapi import APIRouter, Depends
from fastapi.responses import FileResponse
from sqlalchemy.orm import Session
from config.module import get_db
from .module import generation_music, delete_music_db, get_music, show_music_base

router = APIRouter(prefix='/music')

@router.post('/{gallery_id}')
def music(gallery_id : int, db : Session = Depends(get_db)):
    audio_path = generation_music(gallery_id, db)
    return {"message": "Music generated successfully", "audio_path": audio_path}

@router.get('/{gallery_id}')
def listen_music(gallery_id : int, db : Session = Depends(get_db)):
    result = get_music(gallery_id, db)
    return FileResponse(path=result, filename=os.path.basename(result), media_type='audio/wav')

@router.delete('/{gallery_id}')
def delete_music(gallert_id : int, db : Session = Depends(get_db)):
    result = delete_music_db(gallert_id, db)
    return result

@router.get('/{audio_path}')
def show_music(audio_path : str):
    file_path  = show_music_base(audio_path)
    return FileResponse(path=file_path, filename=os.path.basename(file_path), media_type='audio/wav')