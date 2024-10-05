package com.d106.arti.member.controller;

import com.d106.arti.member.service.InstagramAccountService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramAccountController {

    private final InstagramAccountService instagramAccountService;

    // application.properties 또는 application-dev.properties에서 값을 주입받기
    @Value("${spring.security.oauth2.client.registration.instagram.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.instagram.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.instagram.redirect-uri}")
    private String redirectUri;

    // 1. Instagram 인증 리디렉션 URL 생성 (GET /instagram/redirect)
    @GetMapping("/redirect")
    public ResponseEntity<String> redirectToInstagram() {
        // 주입받은 값을 사용하여 리디렉션 URL 생성
        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);
        System.out.println("Redirect URI: " + redirectUri);
        // Instagram 인증 요청 URL 생성
        String instagramAuthUrl = "https://api.instagram.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=user_profile,user_media" +
                "&response_type=code" +
                "&hl=ko";
                ;

        // 리디렉션 URL 반환
        return ResponseEntity.status(302)
                .header("Location", instagramAuthUrl)
                .build();
    }

    // 2. Instagram 인증 코드 받기 및 액세스 토큰 교환 (POST /instagram/auth)
    @PostMapping("/auth")
    public ResponseEntity<String> authenticateInstagram(@RequestParam("code") String code) {
        // code 값 콘솔에 출력 (로그로 확인 가능)
        System.out.println("Received Instagram authorization code: " + code);

        // 인증 코드를 사용하여 액세스 토큰 교환 및 저장
        instagramAccountService.authenticateAndSaveToken(code);

        return ResponseEntity.ok("Instagram account linked successfully.");
    }

    // 3. 비동기 방식으로 Instagram 미디어 URL 가져오기 (GET /instagram/media)
    @GetMapping("/media")
    public Mono<ResponseEntity<String>> getMediaUrls() {
        return instagramAccountService.getInstagramMediaUrls()
                .map(mediaUrls -> ResponseEntity.ok(mediaUrls)); // 비동기 응답 처리
    }
}