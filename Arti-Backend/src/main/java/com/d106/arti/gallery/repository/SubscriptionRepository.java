package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Subscription;
import com.d106.arti.gallery.domain.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    // 특정 회원의 활성화된 구독 목록을 조회
    List<Subscription> findByMemberIdAndIsActiveTrue(Integer memberId); // 활성 구독 조회

    // 특정 회원과 미술관에 대한 구독 조회
    Optional<Subscription> findByMemberIdAndGalleryId(Integer memberId, Integer galleryId);
}
