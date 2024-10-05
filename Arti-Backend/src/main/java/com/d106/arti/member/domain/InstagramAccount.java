package com.d106.arti.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class InstagramAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String instagramEmail; // Instagram 계정의 이메일

    // Member와 1:1 관계 설정
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // OauthToken과 1:1 관계 설정
    @OneToOne(mappedBy = "instagramAccount", cascade = CascadeType.ALL)
    private OauthToken oauthToken;
}