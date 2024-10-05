package com.d106.arti.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class OauthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer memberId; // 토큰을 소유한 사용자의 ID

    @Column(nullable = false)
    private String accessToken; // Instagram 액세스 토큰

    @Column(nullable = false)
    private long expiresIn; // 토큰의 유효 기간 (초 단위)

    // InstagramAccount와 1:1 관계 설정
    @OneToOne
    @JoinColumn(name = "instagram_account_id")
    private InstagramAccount instagramAccount;
}