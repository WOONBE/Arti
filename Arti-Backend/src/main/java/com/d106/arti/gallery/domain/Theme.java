package com.d106.arti.gallery.domain;

import com.d106.arti.artwork.domain.AiArtwork;
import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.ThemeArtwork;
import com.d106.arti.artwork.domain.*;
import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

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
public class Theme {

    @Id
    @Column(name = "THEME_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 갤러리와 n : 1 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GALLERY_ID", nullable = false)
    private Gallery gallery;

    // 미술품과 1:n 관계 설정
    @OneToMany(mappedBy = "theme",  orphanRemoval = false, fetch = FetchType.LAZY)
    private List<ArtworkTheme> artworks;

    // ThemeArtwork와의 1:n 관계 설정 + 단일 상속 전략으로 AI미술품의 ID가 이미 Artwork의 ID이기 때문에 AI테이블과 이 이상 연결될 필요가 없음.
    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThemeArtwork> themeArtworks; // 여러 ThemeArtwork와 연결

    @Column(name = "THEME_TITLE", nullable = false)  // 필드 추가
    private String themeTitle;

    // (추가 주석) 테마의 주인은 theme의 gallery를 통해 gallery의 ownerId로 확인 가능 => extends하면 성능 저하 우려 있다고 함.
    public Member getOwner() {
        return gallery.getOwner();
    }

    public void setThemeTitle(String newTitle) {
    }
}
