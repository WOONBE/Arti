package com.d106.arti.login.dto.request;

import lombok.Getter;

@Getter
public class VerificationRequest {
    private String email;
    private String code;
}
