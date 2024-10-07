package com.d106.arti.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramController {

    @Value("${spring.security.oauth2.client.registration.instagram.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.instagram.redirect-uri}")
    private String redirectUri;

    @GetMapping("/redirect")
    public ResponseEntity<String> redirect() {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.instagram.com/oauth/authorize")
            .queryParam("client_id", clientId).queryParam("redirect_uri", redirectUri)
            .queryParam("scope", "user_profile,user_media").queryParam("response_type", "code")
            .build().toUriString();
        return ResponseEntity.status(302).header("Location", url).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        String codeWithSuffix = code.split("#")[0];
        return ResponseEntity.ok(codeWithSuffix);
    }

    @PostMapping("/save-token")
    public ResponseEntity<?> saveToken(@RequestBody SaveTokenRequest request) {
        return ResponseEntity.ok().build();
    }
}
