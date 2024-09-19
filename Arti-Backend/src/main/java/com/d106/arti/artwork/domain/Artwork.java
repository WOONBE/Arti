package com.d106.arti.artwork.domain;
import com.d106.arti.gallery.domain.Theme;
import com.d106.arti.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Artwork extends BaseEntity {

    @Id
    @Column(name = "ARTWORK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "THEME_ID")
    private Theme theme;

    @Column(name = "ARTWORK_YEAR")
    private Integer year;

    @Column(name = "ARTWORK_TITLE")
    private String title;

    @Column(name = "ARTWORK_IMAGE")
    private String image;

    @Column(name = "ARTWORK_DESC")
    private String description;

    // 작가와의 ManyToOne 관계 설정
    @ManyToOne
    @JoinColumn(name = "ARTIST_ID", nullable = false)
    private Artist artist;


    // ThemeArtwork와의 1:n 관계 설정
    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ThemeArtwork> themeArtworks; // 여러 ThemeArtwork와 연결

}

