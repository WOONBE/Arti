package com.d106.arti.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InstagramTokenRequest {
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String redirectUri;
    private String code;
}