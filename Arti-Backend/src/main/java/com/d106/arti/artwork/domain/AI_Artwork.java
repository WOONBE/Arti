package com.d106.arti.artwork.domain;

import com.d106.arti.global.common.BaseEntity;
import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;

public class AI_Artwork extends BaseEntity {
    @Id
    @Column(name = "AI_ARTWORK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "AI_ARTWORK_TITLE")
    private String title;

    @Column(name = "AI_ARTWORK_IMAGE_URL")
    private String imageUrl;

    @Column(name="is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    private Boolean isDeleted;

    @Column(name = "AI_ARTWORK_THEME", nullable = false)
    private String aiArtworkTheme;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
