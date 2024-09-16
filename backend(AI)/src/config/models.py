from sqlalchemy import Column, ForeignKey, Integer, VARCHAR, Text
from sqlalchemy.sql import func

from .database import Base

class Artwork(Base):
    __tablename__ = "wikiart_data"

    artwork_id = Column(Integer, primary_key = True, index=True)
    artist = Column(VARCHAR(255))
    filename = Column(VARCHAR(255))
    genre = Column(VARCHAR(255))
    description = Column(VARCHAR(255))
    phash = Column(VARCHAR(64))
    width = Column(Integer)
    height = Column(Integer)
    genre_count = Column(Integer)
    subset = Column(VARCHAR(50))
    artist_ko = Column(VARCHAR(255))
    title = Column(VARCHAR(255))
    year = Column(VARCHAR(10))

    artist_id = Column(Integer, ForeignKey("Artist.artist_id"))

class Artist(Base):
    __tablename__ = "artist"

    artist_id = Column(Integer, primary_key=True, index=True)
    artist = Column(VARCHAR(255))
    artist_ko = Column(VARCHAR(255))
    summary = Column(Text)
    image_url = Column(Text)