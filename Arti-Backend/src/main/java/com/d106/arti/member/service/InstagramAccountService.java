package com.d106.arti.member.service;

import com.d106.arti.member.domain.Member;
import com.d106.arti.member.domain.OauthToken;
import com.d106.arti.member.dto.request.InstagramTokenRequest;
import com.d106.arti.member.dto.response.InstagramTokenResponse;
import com.d106.arti.member.repository.InstagramAccountRepository;
import com.d106.arti.member.repository.MemberRepository;
import com.d106.arti.member.repository.OauthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InstagramAccountService {

    private final InstagramAccountRepository instagramAccountRepository;
    private final MemberRepository memberRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.instagram.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.instagram.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.instagram.redirect-uri}")
    private String redirectUri;

    // Instagram 연동 및 이메일 중복 체크
    @Transactional
    public Mono<String> authenticateAndSaveToken(String code) {
        // 현재 로그인한 사용자(Member) 가져오기
//        Member loggedInMember = getCurrentLoggedInMember();
//        Integer memberId = loggedInMember.getId();

        // 액세스 토큰 요청 URL
        String tokenUrl = "https://api.instagram.com/oauth/access_token";

        // POST 요청을 위한 파라미터 설정
        InstagramTokenRequest tokenRequest = new InstagramTokenRequest(clientId, clientSecret,
            "authorization_code", redirectUri, code);

        // WebClient를 사용하여 비동기 방식으로 토큰 요청
        InstagramTokenResponse tokenResponse = webClient.post().uri(tokenUrl)
            .bodyValue(tokenRequest).retrieve().bodyToMono(InstagramTokenResponse.class).block();

        // Instagram API에서 media_url 필드만 가져오기
        String url = "https://graph.instagram.com/me/media?fields=media_url&access_token="
            + tokenResponse.getAccessToken();

        // WebClient로 비동기 방식으로 Instagram API에서 media_url 필드만 가져오기
        return webClient.get().uri(url).retrieve()
            .bodyToMono(String.class); // 응답을 String으로 변환하고 비동기로 처리
//        if (tokenResponse != null) {
//            // 받은 토큰 저장 및 Instagram 계정 연동
//            String instagramUsername = tokenResponse.getUsername();  // username 가져오기
//
//            // InstagramAccount 엔티티에 저장
//            InstagramAccount instagramAccount = InstagramAccount.builder()
//                .instagramEmail(instagramUsername)  // username을 email 대신 사용
//                .member(loggedInMember)  // 현재 로그인한 Member와 연동
//                .build();
//
//            instagramAccountRepository.save(instagramAccount);  // 저장
//        } else {
//            throw new RuntimeException("Failed to retrieve access token from Instagram");
//        }
    }

    // 현재 로그인한 사용자 정보 가져오기
    private Member getCurrentLoggedInMember() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 디버깅을 위한 로그 추가
        System.out.println("Principal: " + principal);

        if (principal instanceof String) {
            String email = (String) principal;
            System.out.println("Email: " + email);  // 이메일 로그 확인
            return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No logged-in member found"));
        }

        if (principal instanceof Member) {
            Member member = (Member) principal;
            System.out.println("Member: " + member);  // Member 객체 로그 확인
            return member;
        }

        throw new RuntimeException("Invalid authentication principal");
    }

    public Mono<String> getInstagramMediaUrls() {
        Member currentMember = getCurrentLoggedInMember(); // 현재 로그인한 회원을 가져옴

        // 회원의 Instagram 계정 정보 가져오기
        OauthToken oauthToken = oauthTokenRepository.findByMemberId(currentMember.getId())
            .orElseThrow(() -> new RuntimeException("No Instagram account linked for this user."));

        // Instagram API에서 media_url 필드만 가져오기
        String url = "https://graph.instagram.com/me/media?fields=media_url&access_token="
            + oauthToken.getAccessToken();

        // WebClient로 비동기 방식으로 Instagram API에서 media_url 필드만 가져오기
        return webClient.get().uri(url).retrieve()
            .bodyToMono(String.class); // 응답을 String으로 변환하고 비동기로 처리
    }
}
