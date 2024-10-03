from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from config.module import get_db
from .module import start, start_save
from .schema import post_start


router = APIRouter(prefix="/start")

@router.get("/")
def cold_start(db : Session = Depends(get_db)):
    result = start(db)
    return result

@router.post('/save')
def cold_start_save(member : post_start, db : Session = Depends(get_db)):
    start_save(member, db)
    return