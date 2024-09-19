from pydantic import BaseModel
from typing import Optional
from uuid import UUID

class post_ai_image(BaseModel):
    id : UUID
    member_id : int
    ai_artowrk_title : str
    ai_img_url : str
    is_deleted : Optional[int] = 0
    
class trasform_image(BaseModel):
    content_image_path : str
    style_image_id : int