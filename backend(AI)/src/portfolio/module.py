from sqlalchemy.orm import Session
from config.models import Gallery, Theme, Artwork, Artwork_Theme, Artist

art_styles_mapping = {
    "Abstract Expressionism": "추상표현주의",
    "Expressionism": "표현주의",
    "Color Field Painting": "컬러 필드",
    "Synthetic Cubism": "종합적 입체파",
    "Minimalism": "미니멀리즘",
    "Pop Art": "팝 아트",
    "Naive Art Primitivism": "나이브 아트",
    "Cubism": "입체주의",
    "Impressionism": "인상주의",
    "Post Impressionism": "탈인상주의",
    "Realism": "사실주의",
    "Fauvism": "야수파",
    "Action painting": "액션페인팅",
    "Pointillism": "점묘법",
    "Contemporary Realism": "현대적 사실주의",
    "Symbolism": "상징주의",
    "Analytical Cubism": "분석적 입체파",
    "Art Nouveau Modern": "아르누보",
    "Romanticism": "낭만주의",
    "New Realism": "새로운 현실주의",
    "Early Renaissance": "르네상스",
    "Baroque": "바로크",
    "Rococo": "로코코",
    "Northern Renaissance": "북방 르네상스",
    "High Renaissance": "전성기 르네상스",
    "Ukiyo e": "우키요에",
    "Mannerism Late Renaissance": "매너리즘"
}

art_styles_list = [
    "추상표현주의",
    "표현주의",
    "컬러 필드",
    "종합적 입체파",
    "미니멀리즘",
    "팝 아트",
    "나이브 아트",
    "입체주의",
    "인상주의",
    "탈인상주의",
    "사실주의",
    "야수파",
    "액션페인팅",
    "점묘법",
    "현대적 사실주의",
    "상징주의",
    "분석적 입체파",
    "아르누보",
    "낭만주의",
    "새로운 현실주의",
    "르네상스",
    "바로크",
    "로코코",
    "북방 르네상스",
    "전성기 르네상스",
    "우키요에",
    "매너리즘"
]

async def get_portfoilo(member_id, db:Session):
    art_styles_dict = {
    "추상표현주의": 0,
    "표현주의": 0,
    "컬러 필드": 0,
    "종합적 입체파": 0,
    "미니멀리즘": 0,
    "팝 아트": 0,
    "나이브 아트": 0,
    "입체주의": 0,
    "인상주의": 0,
    "탈인상주의": 0,
    "사실주의": 0,
    "야수파": 0,
    "액션페인팅": 0,
    "점묘법": 0,
    "현대적 사실주의": 0,
    "상징주의": 0,
    "분석적 입체파": 0,
    "아르누보": 0,
    "낭만주의": 0,
    "새로운 현실주의": 0,
    "르네상스": 0,
    "바로크": 0,
    "로코코": 0,
    "북방 르네상스": 0,
    "전성기 르네상스": 0,
    "우키요에": 0,
    "매너리즘": 0
    }
    data = db.query(Gallery).filter(Gallery.owner_id == member_id).first()
    themes = db.query(Theme).filter(Theme.gallery_id == data.gallery_id).all()
    theme_id = [theme.theme_id for theme in themes]
    artwork_theme_ids = db.query(Artwork_Theme).filter(Artwork_Theme.theme_id.in_(theme_id)).all()

    artwork_ids = [artwork_theme.artwork_id for artwork_theme in artwork_theme_ids]

    artworks = db.query(Artwork).filter(Artwork.artwork_id.in_(artwork_ids)).all()

    for artwork in artworks:
        genre_str = artwork.genre
        if genre_str == None: continue
        genres_cleaned = genre_str.strip("[]").replace("'", "").split(", ")
        for genre in genres_cleaned:
            if genre in art_styles_mapping:
                art_styles_dict[art_styles_mapping[genre]] += 1

    all_genre = []
    
    for genre in art_styles_list:
        if art_styles_dict[genre] != 0:
            data = {
                'genre' : genre,
                'count' : art_styles_dict[genre]
            }
            all_genre.append(data)

    return all_genre