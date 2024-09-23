from sqlalchemy import Column, ForeignKey, Integer, VARCHAR, Text
from sqlalchemy.sql import func

from .database import Base

class Artwork(Base):
    __tablename__ = "wikiart_data"

    artwork_id = Column(Integer, primary_key = True, index=True, autoincrement=True)
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

    artist_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    artist = Column(VARCHAR(255))
    artist_ko = Column(VARCHAR(255))
    summary = Column(Text)
    image_url = Column(Text)

class Member(Base):
    __tablename__ = 'member'

    member_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    email = Column(VARCHAR(255))
    nickname = Column(VARCHAR(255))
    password = Column(VARCHAR(255))
    profile = Column(VARCHAR(255))

class AI_Artwork(Base):
    __tablename__ = 'ai_artwork'

    ai_artwork_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    member_id = Column(Integer, ForeignKey("member.member_id"), nullable=False)
    ai_artwork_title = Column(VARCHAR(255), nullable=False)
    ai_img_url = Column(VARCHAR(255), nullable=False)
    is_deleted = Column(Integer, default=0)

class Gallery(Base):
    __tablename__ = 'gallery'

    gallery_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    gallery_desc = Column(VARCHAR(255))
    gallery_img = Column(VARCHAR(255))
    gallery_name = Column(VARCHAR(255))
    gallery_view = Column(Integer)