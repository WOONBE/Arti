package com.d106.arti.gallery.domain;

import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.*;
import com.d106.arti.global.common.BaseEntity;
import jakarta.persistence.*;

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

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Theme extends BaseEntity {

    @Id
    @Column(name = "THEME_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "THEME_NAME", nullable = false)
    private String name;

    //미술관과 n : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GALLERY_ID", nullable = false)
    private Gallery gallery;

    //미술품과 1:n 부모인 , orphanRemoval을 쓰는 이유?
    //Theme과 Artwork의 연결만 끊고 Artwork 자체는 유지
    @OneToMany(mappedBy = "theme",  orphanRemoval = false, fetch = FetchType.LAZY)
    private List<ArtworkTheme> artworks;

    // 연관관계 메서드: Artwork 추가
    public void addArtwork(Artwork artwork, String description) {
        ArtworkTheme artworkTheme = ArtworkTheme.builder()
            .artwork(artwork)
            .theme(this)
            .description(description)
            .build();

        this.artworks.add(artworkTheme);
        artwork.getArtworkThemes().add(artworkTheme);  // Artwork에도 추가
    }


    // 연관관계 메서드: Artwork 삭제
    public void removeArtwork(Artwork artwork) {
        this.artworks.removeIf(artworkTheme -> artworkTheme.getArtwork().equals(artwork));
        artwork.getArtworkThemes().removeIf(artworkTheme -> artworkTheme.getTheme().equals(this));  // Artwork에서도 삭제
    }

    // Gallery 설정 메서드
    public void updateGallery(Gallery gallery) {
        this.gallery = gallery;
    }


    public void updateName(String name) {
        this.name = name;
    }
}
