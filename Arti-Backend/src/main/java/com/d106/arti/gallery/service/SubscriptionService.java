package com.d106.arti.gallery.service;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.domain.Subscription;
import com.d106.arti.gallery.dto.response.SubscriptionResponse;
import com.d106.arti.gallery.repository.SubscriptionRepository;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final GalleryRepository galleryRepository;

    // 구독한 미술관 목록 조회 (수정됨)
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscribedGalleries(Member member) {
        List<Subscription> subscriptions = subscriptionRepository.findByMemberIdAndIsActiveTrue(member.getId());

        // SubscriptionResponse 생성자에서 Subscription 객체를 받아서 처리
        return subscriptions.stream()
                .map(SubscriptionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 구독 등록 (수정됨)
    @Transactional
    public void subscribe(Member member, Integer galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 갤러리를 찾을 수 없습니다."));

        Subscription subscription = new Subscription(member, gallery);
        subscriptionRepository.save(subscription);
    }

    // 구독 취소 (수정됨)
    @Transactional
    public void unsubscribe(Member member, Integer galleryId) {
        Subscription subscription = subscriptionRepository.findByMemberIdAndGalleryId(member.getId(), galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구독을 찾을 수 없습니다."));

        // 서비스에서 구독 취소 처리
        subscription.deactivate();
        subscriptionRepository.save(subscription);
    }
}
