package com.d106.arti.member.domain;

import com.d106.arti.global.common.BaseEntity;
import com.d106.arti.gallery.domain.Subscription; // 대응 관계 추가함!
import jakarta.persistence.*;

import com.d106.arti.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "image")
    private String image;

//    // 구독과 1:n 관계 설정 (대응 관계 추가함!)
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Subscription> subscriptions;

    @ElementCollection
    private List<Integer> subscribedGalleryIds;

//    // 여기 수정됨: 생성자를 통한 Subscription 추가를 위한 메서드
//    public void addSubscription(Subscription subscription) {
//        subscriptions.add(subscription);
//        subscription.setMember(this); // 양방향 관계 설정을 위함
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
