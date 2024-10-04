import os
from fastapi import APIRouter, Depends, HTTPException
from fastapi.responses import StreamingResponse
from sqlalchemy.orm import Session
from config.module import get_db
from .module import generation_music, delete_music_db, get_music, show_music_base

router = APIRouter(prefix='/music')

@router.post('/{gallery_id}', tags=['music'])
async def music(gallery_id: int, db: Session = Depends(get_db)):
    audio_path = await generation_music(gallery_id, db)
    return {"message": "Music generated successfully", "audio_path": audio_path}

@router.get('/{gallery_id}', tags=['music'])
def listen_music(gallery_id: int, db: Session = Depends(get_db)):
    result = get_music(gallery_id, db)

    # 파일이 존재하는지 확인
    if isinstance(result, dict) and 'error' in result:
        raise HTTPException(status_code=404, detail=result['error'])
    
    if not os.path.exists(result):
        raise HTTPException(status_code=404, detail="Audio file not found")

    # 파일을 스트림 방식으로 열기
    def iterfile():
        with open(result, mode="rb") as file_like:
            yield from file_like

    return StreamingResponse(
        iterfile(),
        media_type='audio/wav'
    )

@router.delete('/{gallery_id}', tags=['music'])
def delete_music(gallery_id: int, db: Session = Depends(get_db)):  # 오타 수정
    result = delete_music_db(gallery_id, db)
    return result

@router.get('/{audio_path:path}', tags=['music'])
def show_music(audio_path: str):
    file_path = show_music_base(audio_path)

    # 파일이 존재하는지 확인
    if isinstance(file_path, dict) and 'error' in file_path:
        raise HTTPException(status_code=404, detail=file_path['error'])
    
    if not os.path.exists(file_path):
        raise HTTPException(status_code=404, detail="File not found")

    # 파일을 스트림 방식으로 열기
    def iterfile():
        with open(file_path, mode="rb") as file_like:
            yield from file_like

    return StreamingResponse(
        iterfile(),
        media_type='audio/wav'
    )