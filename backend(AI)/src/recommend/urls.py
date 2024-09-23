from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from .module import recommend
from config.module import get_db

router = APIRouter(prefix="/home")

@router.get('/')
def get_recommend(db: Session = Depends(get_db)):
    try:
        user_id = 1
        simmilar_galleries, top_view_galleries = recommend(user_id, db)
        
        return {
            'top_viewed_galleries' : top_view_galleries,
            'similary_galleries' : simmilar_galleries
        }
    except Exception as e:
        return HTTPException(status_code=500, detail=str(e))