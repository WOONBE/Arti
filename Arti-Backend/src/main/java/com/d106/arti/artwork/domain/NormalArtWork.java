package com.d106.arti.artwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("NORMAL")
public class NormalArtWork extends Artwork {

//    @Id
//    @Column(name = "ARTIST_ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(name = "filename")
    private String filename;

//    @Column(name = "artist")
//    private String artist;

    @Column(name = "artist_name")
    private String artistName; // artist를 String으로 저장하는 필드

    @Column(name = "genre")
    private String genre;

    @Column(name = "description")
    private String description;

    @Column(name = "phash")
    private String phash;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "genre_count")
    private Integer genreCount;

    @Column(name = "subset")
    private String subset;

    @Column(name = "artist_ko")
    private String artistKo;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private String year;


}
