package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Subscription;
import com.d106.arti.gallery.domain.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    // Optional을 반환하는 메서드 (단일 결과)
    Optional<Subscription> findByIdAndIsDeletedFalse(SubscriptionId subscriptionId);

    // List를 반환하는 메서드 (다수 결과)
    List<Subscription> findByMemberIdAndIsDeletedFalse(Integer memberId);

    List<Subscription> findByGalleryIdAndIsDeletedFalse(Integer galleryId);
}
