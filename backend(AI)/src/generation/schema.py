from pydantic import BaseModel, Field
from typing import Optional
from uuid import UUID, uuid4

class post_ai_image(BaseModel):
    member_id : int
    ai_artwork_title : str
    ai_img_url : str
    is_deleted : Optional[int] = 0

    class Config:
        from_attributes = True
    
class trasform_image(BaseModel):
    content_image_path : str
    style_image_id : int