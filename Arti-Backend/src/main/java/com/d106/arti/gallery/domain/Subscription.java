package com.d106.arti.gallery.domain;

import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
    @JoinColumn(name = "member_id", nullable = false) // 여기 수정됨!
    private Member member;

    // Gallery와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("galleryId")
    @JoinColumn(name = "gallery_id", nullable = false) // 여기 수정됨!
    private Gallery gallery;

    // Member와 Gallery를 인자로 받아 Subscription을 생성하는 생성자
    public Subscription(Member member, Gallery gallery) {
        this.id = new SubscriptionId(member.getId(), gallery.getId());
        this.member = member;
        this.gallery = gallery;
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
