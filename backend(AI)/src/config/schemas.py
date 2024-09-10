from pydantic import BaseModel
from typing import List

class artwork_in(BaseModel):

    artwork_id : int
    artist : str

    class Config:
        from_attributes = True

