package com.d106.arti.instagram.controller;

import com.d106.arti.instagram.service.InstagramAccountService;
import com.d106.arti.member.domain.Member;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramController {

    private final InstagramAccountService instagramAccountService;
    @Value("${spring.security.oauth2.client.registration.instagram.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.instagram.redirect-uri}")
    private String redirectUri;

    @GetMapping("/redirect")
    public ResponseEntity<String> redirect() {
        System.out.println(redirectUri);
        String url = UriComponentsBuilder.fromHttpUrl("https://api.instagram.com/oauth/authorize")
            .queryParam("client_id", clientId).queryParam("redirect_uri", redirectUri)
            .queryParam("scope", "user_profile,user_media").queryParam("response_type", "code")
            .build().toUriString();
        return ResponseEntity.status(302).header("Location", url).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save-token")
    public ResponseEntity<?> saveToken(
        @RequestBody SaveTokenRequest request,
        Principal connectedUser
    ) throws URISyntaxException {
        Map<String, String> queryParams = extractQueryParams(request.getUrl());
        String codeWithoutSuffix = queryParams.get("code").split("#")[0];
        instagramAccountService.authenticateAndSaveToken(codeWithoutSuffix, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/media")
    public ResponseEntity<?> getInstagramMediaUrls(Principal connectedUser) {
        Member currentMember = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return ResponseEntity.ok(
            instagramAccountService.getInstagramMediaUrls(currentMember));
    }

    private static Map<String, String> extractQueryParams(String uriString)
        throws URISyntaxException {
        URI uri = new URI(uriString);
        String query = uri.getQuery();

        Map<String, String> queryParams = new HashMap<>();

        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";
                queryParams.put(key, value);
            }
        }

        return queryParams;
    }
}
