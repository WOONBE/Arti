from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from .module import recommend_gallery, recommend_artwork
from config.module import get_db

router = APIRouter(prefix="/home")

@router.get('/{user_id}', tags=['recommendation'])
def get_recommend(user_id : int, db: Session = Depends(get_db)):
    try:
        result = recommend_gallery(user_id, db)
        
        return result
    except Exception as e:
        return HTTPException(status_code=500, detail=str(e))
    
@router.get('/artwork/{user_id}', tags=['recommendation'])
def get_recommend_artwork(user_id : int, db : Session = Depends(get_db)):
    try:
        result = recommend_artwork(user_id, db)
        
        return result
    except Exception as e:
        return HTTPException(status_code=500, detail=str(e))