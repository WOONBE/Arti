package com.d106.arti.gallery.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SubscriptionId implements Serializable {

    private Integer memberId;
    private Integer galleryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionId that = (SubscriptionId) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(galleryId, that.galleryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, galleryId);
    }
}
