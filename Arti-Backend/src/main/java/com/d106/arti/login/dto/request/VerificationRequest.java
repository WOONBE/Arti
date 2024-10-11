package com.d106.arti.login.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
    private String email;
    private String code;
}
