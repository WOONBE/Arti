package com.d106.arti.artwork.domain;

import com.d106.arti.gallery.domain.Theme;
import com.d106.arti.global.common.BaseEntity;
import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;

@Entity
public class AiArtwork extends BaseEntity {
    @Id
    @Column(name = "AI_ARTWORK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "AI_ARTWORK_TITLE")
    private String title;

    @Column(name = "AI_ARTWORK_IMAGE_URL")
    private String imageUrl;

    @Column(name="IS_DELETED", nullable = false, columnDefinition = "tinyint default 0")
    private Boolean isDeleted;

//    // 이건 수정 필요! => OneToMany임!
//    // AI Artwork와 Theme 간의 관계 설정
//    // AI 작품이 Theme
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "THEME_ID", nullable = false)
//    private Theme theme;
//
//    //이건 OneToOne으로 해야할듯함!
//    // AI Artwork와 Member 간의 관계 설정 (대응 관계 추가함!)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_ID", nullable = false)
//    private Member member;
}
