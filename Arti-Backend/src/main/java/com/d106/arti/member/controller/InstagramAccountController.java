//package com.d106.arti.member.controller;
//
//import com.d106.arti.member.service.InstagramAccountService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/instagram")
//@RequiredArgsConstructor
//public class InstagramAccountController {
//
//    private final InstagramAccountService instagramAccountService;
//
//    @Value("${spring.security.oauth2.client.registration.instagram.client-id}")
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.registration.instagram.client-secret}")
//    private String clientSecret;
//
//    @Value("${spring.security.oauth2.client.registration.instagram.redirect-uri}")
//    private String redirectUri;
//
//    // 1. Instagram 인증 리디렉션 URL 생성 (GET /instagram/redirect)
//    @GetMapping("/redirect")
//    public ResponseEntity<String> redirectToInstagram() {
//        // Instagram 인증 요청 URL 생성
//        String instagramAuthUrl = "https://api.instagram.com/oauth/authorize" +
//                "?client_id=" + clientId +
//                "&redirect_uri=" + redirectUri +
//                "&scope=user_profile,user_media" +
//                "&response_type=code" +
//                "&hl=ko";
//
//        // 리디렉션 URL 반환
//        return ResponseEntity.status(302)
//                .header("Location", instagramAuthUrl)
//                .build();
//    }
//
//    // 2. Instagram 인증 코드 받기 및 액세스 토큰 교환 (GET /instagram/callback)
//    @GetMapping("/callback")
//    public ResponseEntity<String> handleInstagramCallback(@RequestParam("code") String code) {
//        System.out.println("Received Instagram authorization code (with #_): " + code);
//
//        // #_ 이후를 제거하여 실제 코드만 남기기
//        if (code.contains("#")) {
//            code = code.split("#")[0]; // #_ 또는 # 이후의 모든 부분 제거
//        }
//
//        System.out.println("Processed Instagram authorization code (cleaned): " + code);
//
//        // `code`를 사용해 토큰 교환 및 Instagram 계정 연동 로직 호출
//        instagramAccountService.authenticateAndSaveToken(code);
//
//        return ResponseEntity.ok("Instagram account linked successfully.");
//    }
//
//    // 3. 비동기 방식으로 Instagram 미디어 URL 가져오기 (GET /instagram/media)
//    @GetMapping("/media")
//    public Mono<ResponseEntity<String>> getMediaUrls() {
//        return instagramAccountService.getInstagramMediaUrls()
//                .map(mediaUrls -> ResponseEntity.ok(mediaUrls)); // 비동기 응답 처리
//    }
//}
