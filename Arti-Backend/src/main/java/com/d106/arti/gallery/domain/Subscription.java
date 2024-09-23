package com.d106.arti.gallery.domain;

import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("galleryId")
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt;

    // `unsubscribedAt` 필드를 삭제 (수정됨)
    // @Column(name = "unsubscribed_at")
    // private LocalDateTime unsubscribedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Subscription(Member member, Gallery gallery) {
        this.id = new SubscriptionId(member.getId(), gallery.getId());
        this.member = member;
        this.gallery = gallery;
        this.subscribedAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
        // `unsubscribedAt` 필드 관련 처리 제거 (수정됨)
        // this.unsubscribedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.subscribedAt = LocalDateTime.now();
        // this.unsubscribedAt = null; (수정됨)
    }
}
