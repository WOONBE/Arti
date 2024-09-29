package com.d106.arti.login.controller;

import com.d106.arti.login.service.MailService;
import com.d106.arti.login.service.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class EmailAuthController {

    private final MailService mailService;

    @Autowired
    public EmailAuthController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping(value = "/request-sign-up")
    public ResponseEntity<?> requestSignUp(@RequestBody RegisterRequest registerRequest) {
        // 회원가입 시 이메일 인증 코드를 전송하는 메소드
        String email = registerRequest.getEmail();

        // MailService에서 메일 전송 로직을 호출
        String code = mailService.sendMail(email);

        return ResponseEntity.ok("인증 코드가 전송되었습니다. 코드: " + code);
    }
}
