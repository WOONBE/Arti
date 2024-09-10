package com.d106.arti.gallery.domain;

import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "subscription", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "gallery_id"})
})
public class Subscription implements Serializable {

    @EmbeddedId
    private SubscriptionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("galleryId")
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

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