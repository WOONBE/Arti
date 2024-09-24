package com.d106.arti.artwork.domain;

import com.d106.arti.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
    private String engName;

    @Column(name = "ARTIST_KOR_NAME")
    private String korName;

    @Column(name = "ARTIST_IMAGE", columnDefinition = "LONGTEXT")
    private String image;

    @Column(name = "ARTIST_SUMMARY",columnDefinition = "LONGTEXT")
    private String summary;

    // Artwork와 1:N 관계
    @OneToMany(mappedBy = "artist")
    private List<Artwork> artworks;

    // 작품 추가 메서드
    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
        artwork.updateArtist(this);
    }



}
