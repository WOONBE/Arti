from datetime import datetime

class Artwork:
    def __init__(self, artwork_id, artwork_type, create_date, update_date, ai_artwork_title, ai_artwork_img, 
                 ai_genre, artist_ko, artist_name, description, filename, genre, genre_count, height, 
                 phash, subset, title, width, year, artist_id, member_id, original_artwork_id, theme_id):
        self.artwork_id = artwork_id
        self.artwork_type = artwork_type
        self.create_date = create_date
        self.update_date = update_date
        self.ai_artwork_title = ai_artwork_title
        self.ai_artwork_img = ai_artwork_img
        self.ai_genre = ai_genre
        self.artist_ko = artist_ko
        self.artist_name = artist_name
        self.description = description
        self.filename = filename
        self.genre = genre
        self.genre_count = genre_count
        self.height = height
        self.phash = phash
        self.subset = subset
        self.title = title
        self.width = width
        self.year = year
        self.artist_id = artist_id
        self.member_id = member_id
        self.original_artwork_id = original_artwork_id
        self.theme_id = theme_id

class Artist:
    def __init__(self, artist_id, create_date, update_date, artist_eng_name, artist_kor_name, artist_summary, artist_image):
        self.artist_id = artist_id
        self.create_date = create_date
        self.update_date = update_date
        self.artist_eng_name = artist_eng_name
        self.artist_kor_name = artist_kor_name
        self.artist_summary = artist_summary
        self.artist_image = artist_image

class Member:
    def __init__(self, member_id, create_date, update_date, email, nickname, password, image):
        self.member_id = member_id
        self.create_date = create_date
        self.update_date = update_date
        self.email = email
        self.nickname = nickname
        self.password = password
        self.image = image

class Gallery:
    def __init__(self, gallery_id, create_date, update_date, gallery_desc, gallery_img, gallery_title, gallery_view, owner_id):
        self.gallery_id = gallery_id
        self.create_date = create_date
        self.update_date = update_date
        self.gallery_desc = gallery_desc
        self.gallery_img = gallery_img
        self.gallery_title = gallery_title
        self.gallery_view = gallery_view
        self.owner_id = owner_id

class Theme:
    def __init__(self, theme_id, gallery_id, create_date, update_date, theme_name):
        self.theme_id = theme_id
        self.gallery_id = gallery_id
        self.create_date = create_date
        self.update_date = update_date
        self.theme_name = theme_name

class Artwork_Theme:
    def __init__(self, id, artwork_id, description, theme_id):
        self.id = id
        self.artwork_id = artwork_id
        self.description = description
        self.theme_id = theme_id

class Audio:
    def __init__(self, audio_id, audio_path, gallery_id):
        self.audio_id = audio_id
        self.audio_path = audio_path
        self.gallery_id = gallery_id

class Cold_Start:
    def __init__(self, id, artwork_id, member_id):
        self.id = id
        self.artwork_id = artwork_id
        self.member_id = member_id
