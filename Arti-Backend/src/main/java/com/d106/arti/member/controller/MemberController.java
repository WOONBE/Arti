package com.d106.arti.member.controller;

import com.d106.arti.instagram.service.InstagramAccountService;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import com.d106.arti.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;
    private final MemberRepository memberRepository;
    private final InstagramAccountService instagramAccountService;

    @PostMapping("/change-password")
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

    @GetMapping("/nickname")
    public ResponseEntity<?> getNickname(Principal connectedUser) {
        return ResponseEntity.ok(service.getNickname(connectedUser));
    }

    @GetMapping("/{id}/instagram/media")
    public ResponseEntity<?> getInstagramMedia(@PathVariable Integer id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(instagramAccountService.getInstagramMediaUrls(member));
    }

    @PostMapping("/{memberId}/subscribe/{galleryId}")
    @Operation(summary = "미술관 구독", description = "특정 미술관을 구독하는 API")
    public ResponseEntity<Map<String, Object>> subscribeGallery(@PathVariable Integer memberId, @PathVariable Integer galleryId) {
        Map<String, Object> response = service.subscribeGallery(memberId, galleryId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{memberId}/unsubscribe/{galleryId}")
    @Operation(summary = "미술관 구독 취소", description = "특정 미술관의 구독을 취소하는 API")
    public ResponseEntity<Map<String, Object>> unsubscribeGallery(@PathVariable Integer memberId, @PathVariable Integer galleryId) {
        Map<String, Object> response = service.unsubscribeGallery(memberId, galleryId);
        return ResponseEntity.ok(response);

    }
}
