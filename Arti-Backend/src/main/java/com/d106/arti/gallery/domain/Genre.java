package com.d106.arti.gallery.domain;

public enum Genre {
    ABSTRACT_EXPRESSIONISM(1, "Abstract_Expressionism", "추상표현주의"),
    EXPRESSIONISM(2, "Expressionism", "표현주의"),
    COLOR_FIELD_PAINTING(3, "Color_Field_Painting", "컬러 필드"),
    SYNTHETIC_CUBISM(4, "Synthetic_Cubism", "종합적 입체파"),
    MINIMALISM(5, "Minimalism", "미니멀리즘"),
    POP_ART(6, "Pop_Art", "팝 아트"),
    NAIVE_ART_PRIMITIVISM(7, "Naive_Art_Primitivism", "나이브 아트"),
    CUBISM(8, "Cubism", "입체주의"),
    IMPRESSIONISM(9, "Impressionism", "인상주의"),
    POST_IMPRESSIONISM(10, "Post_Impressionism", "탈인상주의"),
    REALISM(11, "Realism", "사실주의"),
    FAUVISM(12, "Fauvism", "야수파"),
    ACTION_PAINTING(13, "Action_painting", "액션페인팅"),
    POINTILLISM(14, "Pointillism", "점묘법"),
    CONTEMPORARY_REALISM(15, "Contemporary_Realism", "현대적 사실주의"),
    SYMBOLISM(16, "Symbolism", "상징주의"),
    ANALYTICAL_CUBISM(17, "Analytical_Cubism", "분석적 입체파"),
    ART_NOUVEAU_MODERN(18, "Art_Nouveau_Modern", "아르누보"),
    ROMANTICISM(19, "Romanticism", "낭만주의"),
    NEW_REALISM(20, "New_Realism", "새로운 현실주의"),
    EARLY_RENAISSANCE(21, "Early_Renaissance", "르네상스"),
    BAROQUE(22, "Baroque", "바로크"),
    ROCOCO(23, "Rococo", "로코코"),
    NORTHERN_RENAISSANCE(24, "Northern_Renaissance", "북방 르네상스"),
    HIGH_RENAISSANCE(25, "High_Renaissance", "전성기 르네상스"),
    UKIYO_E(26, "Ukiyo_e", "우키요에"),
    MANNERISM_LATE_RENAISSANCE(27, "Mannerism_Late_Renaissance", "매너리즘");

    private final int id;
    private final String label;
    private final String labelKo;

    Genre(int id, String label, String labelKo) {
        this.id = id;
        this.label = label;
        this.labelKo = labelKo;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getLabelKo() {
        return labelKo;
    }

    public static Genre fromId(int id) {
        for (Genre genre : values()) {
            if (genre.getId() == id) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Invalid genre ID: " + id);
    }
}
