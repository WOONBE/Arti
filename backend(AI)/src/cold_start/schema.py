from pydantic import BaseModel
from uuid import UUID
from typing import List

class Artwork_cold_start(BaseModel):
    artwork_id : int
    title : str
    filename : str

class Cold_start(BaseModel):
    artwork_id : int
    member_id : int

class post_start(BaseModel):
    lst : str
    member_id : int