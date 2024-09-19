package com.d106.arti.member.domain;

import com.d106.arti.global.common.BaseEntity;
import com.d106.arti.gallery.domain.Subscription; // 대응 관계 추가함!
import jakarta.persistence.*;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Integer id;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NICKNAME", unique = true, nullable = false)
    private String nickname;

    @Column(name = "IMAGE")
    private String image;

    // 구독과 1:n 관계 설정 (대응 관계 추가함!)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    // 여기 수정됨: 생성자를 통한 Subscription 추가를 위한 메서드
    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setMember(this); // 양방향 관계 설정을 위함
    }
}
