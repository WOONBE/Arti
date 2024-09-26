package com.d106.arti.member.controller;

import com.d106.arti.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 미술관 구독 API
    @PostMapping("/{memberId}/subscribe/{galleryId}")
    public ResponseEntity<String> subscribeGallery(@PathVariable Integer memberId, @PathVariable Integer galleryId) {
        memberService.subscribeGallery(memberId, galleryId);
        return ResponseEntity.ok("미술관 구독이 완료되었습니다.");
    }

    // 미술관 구독 취소 API
    @DeleteMapping("/{memberId}/unsubscribe/{galleryId}")
    public ResponseEntity<String> unsubscribeGallery(@PathVariable Integer memberId, @PathVariable Integer galleryId) {
        memberService.unsubscribeGallery(memberId, galleryId);
        return ResponseEntity.ok("미술관 구독 취소가 완료되었습니다.");
    }
}