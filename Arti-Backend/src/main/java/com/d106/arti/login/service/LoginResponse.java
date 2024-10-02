package com.d106.arti.login.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LoginResponse {

    private String token;
    private long expiresIn;
    private Integer memberId;
    private Integer galleryId;
}
