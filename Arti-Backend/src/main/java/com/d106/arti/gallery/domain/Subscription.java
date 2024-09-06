package com.d106.arti.gallery.domain;

import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Entity
@RequiredArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GALLERY_ID")
    private Gallery gallery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

}
