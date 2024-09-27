from sqlalchemy.orm import Session
from config.module import get_db
from config.models import Gallery, Theme, Artwork


def get_portfoilo(member_id, db:Session):
    data = db.query(Gallery).filter(Gallery.owner_id == member_id).first()
    