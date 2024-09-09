from pydantic import BaseModel
from uuid import UUID

class Gallery(BaseModel):
    id: UUID
    gallery_title: str
    gallery_view: int
    gallery_owner: str
    gallery_image: str
    gallery_description: str

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
    