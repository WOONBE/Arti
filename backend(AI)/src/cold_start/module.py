from sqlalchemy.orm import Session
from sqlalchemy.sql.expression import func
from config.models import Cold_Start, Artwork
from typing import List
import random
from .schema import Artwork_cold_start, post_start


import json

def start(db : Session):
    data = db.query(Artwork).order_by(func.random()).limit(45).all()

    if len(data) < 45:
        return {"error": "Not enough data to sample 9 items 5 times."}
    
    result = []
    
    # 샘플링된 데이터에서 Artwork_cold_start 객체 생성
    for artwork in data:
        data = Artwork_cold_start(
            artwork_id=artwork.artwork_id,
            title=artwork.title,
            filename='https://j11d106.p.ssafy.io/static/' + artwork.filename
        )
        result.append(data)

    return result

def start_save(start : post_start, db : Session):
    artwork_ids = json.loads(start.lst)
    
    # 각 artwork_id에 대해 Cold_Start 레코드 생성
    for artwork_id in artwork_ids:
        data = Cold_Start(
            artwork_id=artwork_id,
            member_id=start.member_id
        )
        db.add(data)
    
    db.commit()