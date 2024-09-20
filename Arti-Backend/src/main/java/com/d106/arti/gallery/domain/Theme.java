package com.d106.arti.gallery.domain;

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

    //미술관과 n : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GALLERY_ID", nullable = false)
    private Gallery gallery;

    //미술품과 1:n 부모인 , orphanRemoval을 쓰는 이유?
    //Theme과 Artwork의 연결만 끊고 Artwork 자체는 유지
    @OneToMany(mappedBy = "theme",  orphanRemoval = false, fetch = FetchType.LAZY)
    private List<ArtworkTheme> artworks;

    //화가와 n : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTIST_ID", nullable = false)
    private Artist artist;

//    //AI 미술품과 1:n 부모인 , orphanRemoval을 쓰는 이유?
//    //Theme과 AI_Artwork의 연결만 끊고 AI_Artwork 자체는 유지
//    @OneToMany(mappedBy = "theme",  orphanRemoval = false, fetch = FetchType.LAZY)
//    private List<AiArtwork> aiArtworks;

    //회원 ID와 n : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    // 수정 2트: ThemeArtwork와의 1:n 관계 설정
    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThemeArtwork> themeArtworks; // 여러 ThemeArtwork와 연결

}
