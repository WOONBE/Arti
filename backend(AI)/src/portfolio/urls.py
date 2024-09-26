from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from config.module import get_db

router = APIRouter('portfolio')

@router.get('{member_id}')
def profile(member_id : int, db : Session = Depends(get_db)):
    return {
        "test" : "준비 중"
    }