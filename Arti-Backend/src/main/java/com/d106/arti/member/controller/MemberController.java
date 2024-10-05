package com.d106.arti.member.controller;

import com.d106.arti.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-nickname")
    public ResponseEntity<?> changeNickname(
            @RequestBody ChangeNicknameRequest request,
            Principal connectedUser
    ) {
        service.changeNickname(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    // 미술관 구독 API
    @PostMapping("/{memberId}/subscribe/{galleryId}")
    @Operation(summary = "미술관 구독", description = "특정 미술관을 구독하는 API")
    public ResponseEntity<String> subscribeGallery(@PathVariable Integer memberId,
                                                   @PathVariable Integer galleryId) {
        String responseMessage = service.subscribeGallery(memberId, galleryId);
        return ResponseEntity.ok(responseMessage);
    }

    // 미술관 구독 취소 API
    @DeleteMapping("/{memberId}/unsubscribe/{galleryId}")
    @Operation(summary = "미술관 구독 취소", description = "특정 미술관을 구독 취소하는 API")
    public ResponseEntity<String> unsubscribeGallery(@PathVariable Integer memberId,
                                                     @PathVariable Integer galleryId) {
        String responseMessage = service.unsubscribeGallery(memberId, galleryId);
        return ResponseEntity.ok(responseMessage);
    }
}
