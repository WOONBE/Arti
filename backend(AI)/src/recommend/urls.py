from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from .module import recommend
from config.module import get_db

router = APIRouter(prefix="/fastapi/home")

@router.get('/{user_id}')
def get_recommend(user_id : int, db: Session = Depends(get_db)):
    try:
        result = recommend(user_id, db)
        
        return result
    except Exception as e:
        return HTTPException(status_code=500, detail=str(e))