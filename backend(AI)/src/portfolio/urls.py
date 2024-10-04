from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session


from config.module import get_db
from .module import get_portfoilo

router = APIRouter(prefix='/portfolio')

@router.get('/{member_id}', tags=['portfolio'])
def profile(member_id : int, db : Session = Depends(get_db)):
    result = get_portfoilo(member_id, db)
    return result
