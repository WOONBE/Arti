package com.d106.arti.artwork.domain;

import com.d106.arti.global.common.BaseEntity;
import jakarta.persistence.Column;
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
public class Artist extends BaseEntity {

    @Id
    @Column(name = "ARTIST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ARTIST_ENG_NAME")
    private String eng_name;

    @Column(name = "ARTIST_KOR_NAME")
    private String kor_name;

    @Column(name = "ARTIST_IMAGE")
    private String image;

    @Column(name = "ARTIST_DESC")
    private String description;



}
