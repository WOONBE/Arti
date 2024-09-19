from sqlalchemy.orm import Session
from config.models import AI_Artwork
from .model import post_ai_image

def insert_post(post_ai : post_ai_image, db: Session):
    post = AI_Artwork(
        ai_artwork = post_ai_image.member_id,
        ai_artwork_title = post_ai_image.ai_artowrk_title,
        ai_img_url = post_ai_image.ai_img_url,
        is_delete = post_ai_image.is_deleted
    )

    db.add(post)
    db.commit()

    return post