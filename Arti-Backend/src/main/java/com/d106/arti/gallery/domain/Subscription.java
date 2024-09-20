package com.d106.arti.gallery.domain;

import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "subscription", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "gallery_id"})
})
public class Subscription implements Serializable {

    @EmbeddedId
    private SubscriptionId id;

    // Member와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Gallery와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("galleryId")
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    // 구독한 시간을 저장하는 필드
    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt;

    // 구독 취소 시간을 저장하는 필드
    @Column(name = "unsubscribed_at")
    private LocalDateTime unsubscribedAt;

    // 구독이 활성 상태인지 여부를 나타내는 필드
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // Member와 Gallery를 인자로 받아 Subscription을 생성하는 생성자
    public Subscription(Member member, Gallery gallery) {
        this.id = new SubscriptionId(member.getId(), gallery.getId());
        this.member = member;
        this.gallery = gallery;
        this.subscribedAt = LocalDateTime.now();
        this.isActive = true; // 기본적으로 구독은 활성 상태로 생성
    }

    // 구독 취소 메서드 => 나중에 Service로 이동할 예정
    public void cancelSubscription() {
        this.isActive = false;
        this.unsubscribedAt = LocalDateTime.now();
    }

    // 재구독 메서드 => 나중에 Service로 이동할 예정
    public void resubscribe() {
        this.isActive = true;
        this.subscribedAt = LocalDateTime.now(); // 재구독 시 새로 구독 시간을 설정
        this.unsubscribedAt = null; // 취소 시간을 초기화
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(member.getId(), that.member.getId()) &&
                Objects.equals(gallery.getId(), that.gallery.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(member.getId(), gallery.getId());
    }
}
