package com.d106.arti.gallery.controller;

import com.d106.arti.gallery.dto.response.SubscriptionResponse;
import com.d106.arti.gallery.service.SubscriptionService;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    // 구독한 미술관 목록 조회
    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getSubscribedGalleries() {
        Member member = userService.getCurrentUser();
        List<SubscriptionResponse> subscribedGalleries = subscriptionService.getSubscribedGalleries(member);
        return ResponseEntity.ok(subscribedGalleries);
    }

    // 구독 등록
    @PostMapping("/{galleryId}/subscribe")
    public ResponseEntity<Void> subscribe(@PathVariable Integer galleryId) {
        Member member = userService.getCurrentUser();
        subscriptionService.subscribe(member, galleryId);
        return ResponseEntity.ok().build();
    }

    // 구독 취소
    @PostMapping("/{galleryId}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable Integer galleryId) {
        Member member = userService.getCurrentUser();
        subscriptionService.unsubscribe(member, galleryId);
        return ResponseEntity.ok().build();
    }
}
