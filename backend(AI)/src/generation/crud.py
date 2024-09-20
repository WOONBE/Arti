from sqlalchemy.orm import Session
from config.models import AI_Artwork
from .model import post_ai_image

def insert_post(post_ai : post_ai_image, db: Session):
    post = AI_Artwork(
        member_id = post_ai.member_id,
        ai_artwork_title = post_ai.ai_artwork_title,
        ai_img_url = post_ai.ai_img_url,
        is_deleted = post_ai.is_deleted
    )

    db.add(post)
    db.commit()
    db.refresh(post)

    return post