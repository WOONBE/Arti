package com.d106.arti.artwork.domain;


import com.d106.arti.gallery.domain.Theme;
import com.d106.arti.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("AI")
public class AiArtwork extends Artwork {

    @Column(name = "AI_ARTWORK_TITLE")
     private String aiArtworkTitle;


    //생성한 그림 저장한 경로
    @Column(name = "AI_ARTWORK_IMG")
    private String artworkImage;


    //받아온 그림 id
    @ManyToOne
    @JoinColumn(name = "ORIGINAL_ARTWORK_ID")
    private Artwork originalImage;


    //장르(아직 db 없음)
    @Column(name = "AI_GENRE")
    private String genre;


    //사용자와 연결,사용자 id(양방향 필요성 아직은 x)
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // 사용자와의 외래 키
    private Member member;


    //테마와 연결, 테마 id(테마와 양방향 매핑)
    @ManyToOne
    @JoinColumn(name = "THEME_ID") // 테마와의 외래 키
    private Theme theme;

    @Column(name = "AI_YEAR")
    @Builder.Default
    private String year = String.valueOf(LocalDate.now().getYear());

//    // 테마와 연관관계 메서드
//    public void updateTheme(Theme theme) {
//        this.theme = theme;
//        if (!theme.getArtworks().contains(this)) {
//            theme.addArtwork(this);
//        }
//    }
//
//    public void removeTheme() {
//        if (this.theme != null) {
//            this.theme.removeArtwork(this);
//            this.theme = null;
//        }
//    }





}
