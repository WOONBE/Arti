package com.d106.arti.instagram.service;

import com.d106.arti.instagram.domain.InstagramAccount;
import com.d106.arti.instagram.repository.InstagramAccountRepository;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.dto.response.InstagramTokenResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InstagramAccountService {

    private final InstagramAccountRepository instagramAccountRepository;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.instagram.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.instagram.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.instagram.redirect-uri}")
    private String redirectUri;

    // Instagram 연동 및 이메일 중복 체크
    @Transactional
    public void authenticateAndSaveToken(String code, Principal connectedUser) {
        // 현재 로그인한 사용자(Member) 가져오기
        Member member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // 액세스 토큰 요청 URL
        String tokenUrl = "https://api.instagram.com/oauth/access_token";

        // POST 요청을 위한 파라미터 설정
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        parameters.add("grant_type", "authorization_code");
        parameters.add("redirect_uri", redirectUri);
        parameters.add("code", code);

        // WebClient를 사용하여 비동기 방식으로 토큰 요청
        InstagramTokenResponse tokenResponse = webClient.post().uri(tokenUrl)
            .bodyValue(parameters).retrieve().bodyToMono(InstagramTokenResponse.class).block();

        if (tokenResponse != null) {
            // 받은 토큰 저장 및 Instagram 계정 연동
            String instagramUsername = tokenResponse.getUsername();  // username 가져오기

            // InstagramAccount 엔티티에 저장
            InstagramAccount instagramAccount = InstagramAccount.builder()
                .instagramUsername(instagramUsername)  // username을 email 대신 사용
                .member(member)  // 현재 로그인한 Member와 연동
                .build();

            instagramAccountRepository.save(instagramAccount);  // 저장


        } else {
            throw new RuntimeException("Failed to retrieve access token from Instagram");
        }
    }

    public Mono<String> getInstagramMediaUrls(Principal connectedUser) {
        Member currentMember = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // 회원의 Instagram 계정 정보 가져오기
        InstagramAccount instagramAccount = instagramAccountRepository.findByMember(currentMember)
            .orElseThrow(() -> new RuntimeException("No Instagram account linked for this user."));

        // Instagram API에서 media_url 필드만 가져오기
        String url = "https://graph.instagram.com/me/media?fields=media_url&access_token="
            + instagramAccount.getAccessToken();

        // WebClient로 비동기 방식으로 Instagram API에서 media_url 필드만 가져오기
        return webClient.get().uri(url).retrieve()
            .bodyToMono(String.class); // 응답을 String으로 변환하고 비동기로 처리
    }
}
