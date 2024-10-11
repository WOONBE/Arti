from pydantic import BaseModel
from uuid import UUID
from typing import List, Optional

class Artwork(BaseModel):
    id: UUID
    artist_id: int
    artwork_title: str
    artwork_img_url: str
    artwokr_year: str
    phash: str
    artwork_genre_count: int
    artwork_train_test: str
    artwork_width: int
    artwork_height: int
    
class GalleryBase(BaseModel):
    id: Optional[int] = None
    gallery_id: int
    gallery_title: Optional[str] = None
    gallery_desc: Optional[str] = None
    gallery_img: str
    gallery_view: int

class Owner(BaseModel):
    id: Optional[int] = None
    member_id : int
    email: str
    nickname: str

class ArtworksBase(BaseModel):
    id: Optional[int] = None
    artwork_id : int
    description : Optional[str] = None
    year : Optional[str] = None
    title : Optional[str] = None
    image_url : str
    
class ThemeBase(BaseModel):
    id: Optional[int] = None
    theme_id : int
    theme_name : str
    artworks : List[ArtworksBase]
    




