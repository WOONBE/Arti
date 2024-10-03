from pydantic import BaseModel, Field
from typing import Optional
from uuid import UUID, uuid4

class post_ai_image(BaseModel):
    member_id : int
    artwork_type : str
    ai_artwork_title : str
    ai_img_url : str

    class Config:
        from_attributes = True
    
class trasform_image(BaseModel):
    content_image_path : str
    style_image_id : int