package com.d106.arti.member.controller;

import com.d106.arti.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "미술관 구독", description = "특정 미술관을 구독하는 API")
    public ResponseEntity<String> subscribeGallery(@PathVariable Integer memberId, @PathVariable Integer galleryId) {
        String responseMessage = memberService.subscribeGallery(memberId, galleryId);
        return ResponseEntity.ok(responseMessage);
    }

    // 미술관 구독 취소 API
    @DeleteMapping("/{memberId}/unsubscribe/{galleryId}")
    @Operation(summary = "미술관 구독 취소", description = "특정 미술관을 구독 취소하는 API")
    public ResponseEntity<String> unsubscribeGallery(@PathVariable Integer memberId, @PathVariable Integer galleryId) {
        String responseMessage = memberService.unsubscribeGallery(memberId, galleryId);
        return ResponseEntity.ok(responseMessage );
    }
}