from sqlalchemy import Column, ForeignKey, Integer, VARCHAR, Text, DateTime
from sqlalchemy.orm import relationship
from .database import Base

class Artwork(Base):
    __tablename__ = "artwork"

    artwork_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    artwork_type = Column(VARCHAR(31))
    create_date = Column(DateTime)
    update_date = Column(DateTime(timezone=True))
    ai_artwork_title = Column(VARCHAR(255))
    ai_artwork_img = Column(VARCHAR(255))
    ai_genre = Column(VARCHAR(255))
    artist_ko = Column(VARCHAR(255))
    artist_name = Column(VARCHAR(255))
    description = Column(VARCHAR(255))
    filename = Column(VARCHAR(255))
    genre = Column(VARCHAR(255))
    genre_count = Column(Integer)
    height = Column(Integer)
    phash = Column(VARCHAR(255))
    subset = Column(VARCHAR(255))
    title = Column(VARCHAR(255))
    width = Column(Integer)
    year = Column(VARCHAR(255))

    # Foreign Key columns
    artist_id = Column(Integer, ForeignKey("artist.artist_id"))
    member_id = Column(Integer, ForeignKey("member.member_id"))
    original_artwork_id = Column(Integer, ForeignKey("artwork.artwork_id"))
    theme_id = Column(Integer, ForeignKey("theme.theme_id"))

    artist = relationship("Artist", back_populates="artworks")
    member = relationship("Member", back_populates="artworks")
    theme = relationship("Theme", back_populates="artworks")
    artwork_theme = relationship("Artwork_Theme", back_populates="artwork")
    cold_start = relationship("Cold_Start", back_populates="artwork")

class Artist(Base):
    __tablename__ = "artist"

    artist_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    create_date = Column(DateTime)
    update_date = Column(DateTime(timezone=True))
    artist_eng_name = Column(VARCHAR(255))
    artist_kor_name = Column(VARCHAR(255))
    artist_summary = Column(Text)
    artist_image = Column(Text)

    artworks = relationship("Artwork", back_populates="artist")

class Member(Base):
    __tablename__ = 'member'

    member_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    create_date = Column(DateTime)
    update_date = Column(DateTime(timezone=True))
    email = Column(VARCHAR(255))
    nickname = Column(VARCHAR(255))
    password = Column(VARCHAR(255))

    artworks = relationship("Artwork", back_populates="member")
    galleries = relationship("Gallery", back_populates="member")
    cold_start = relationship("Cold_Start", back_populates="member")
    # themes = relationship("Theme", back_populates="member")

class Gallery(Base):
    __tablename__ = 'gallery'

    gallery_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    create_date = Column(DateTime)
    update_date = Column(DateTime(timezone=True))
    gallery_desc = Column(VARCHAR(255))
    gallery_img = Column(VARCHAR(255))
    gallery_title = Column(VARCHAR(255))
    gallery_view = Column(Integer)

    owner_id = Column(Integer, ForeignKey("member.member_id"))

    member = relationship("Member", back_populates="galleries")
    audio = relationship("Audio", back_populates='gallery')

class Theme(Base):
    __tablename__ = 'theme'

    theme_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    gallery_id = Column(Integer, ForeignKey("gallery.gallery_id"))
    create_date = Column(DateTime)
    update_date = Column(DateTime(timezone=True))
    theme_name = Column(VARCHAR(50))

    artworks = relationship("Artwork", back_populates="theme")
    # member = relationship("Member", back_populates="themes")
    artwork_theme = relationship("Artwork_Theme", back_populates="theme")

class Artwork_Theme(Base):
    __tablename__ = 'artwork_theme'

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    artwork_id = Column(Integer, ForeignKey("artwork.artwork_id"))
    description = Column(VARCHAR(255))
    theme_id = Column(Integer, ForeignKey("theme.theme_id"))

    artwork = relationship("Artwork", back_populates="artwork_theme")
    theme = relationship("Theme", back_populates="artwork_theme")

class Audio(Base):
    __tablename__ = 'audio'

    audio_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    audio_path = Column(VARCHAR(255))
    gallery_id = Column(Integer, ForeignKey("gallery.gallery_id"))

    gallery = relationship("Gallery", back_populates="audio")

class Cold_Start(Base):
    __tablename__='cold_start'

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    artwork_id = Column(Integer, ForeignKey("artwork.artwork_id"))
    member_id = Column(Integer, ForeignKey("member.member_id"))

    artwork = relationship("Artwork", back_populates="cold_start")
    member = relationship("Member", back_populates="cold_start")
