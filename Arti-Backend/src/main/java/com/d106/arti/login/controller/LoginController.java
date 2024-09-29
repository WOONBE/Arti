package com.d106.arti.login.controller;

import com.d106.arti.login.dto.request.VerificationRequest;
import com.d106.arti.login.service.*;
import com.d106.arti.storage.StorageService;
import com.d106.arti.storage.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final JwtService jwtService;
    private final LoginService loginService;
    private final StorageService storageService;
    private final MailService mailService;
    private final VerificationService verificationService;

    // 1. 회원가입 요청 (이메일 인증번호 전송)
    @PostMapping("/request-auth-number")
    public ResponseEntity<?> requestSignUp(@RequestBody RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String code = mailService.sendMail(email);
        verificationService.saveVerificationCode(email, code);  // 인증번호 저장
        return ResponseEntity.ok("인증 코드가 전송되었습니다. 이메일: " + email);
    }

    // 2. 이메일 인증 (인증번호 확인)
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        boolean isValid = verificationService.verifyCode(verificationRequest.getEmail(), verificationRequest.getCode());

        if (isValid) {
            return ResponseEntity.ok("인증 성공, 회원가입을 계속 진행하세요.");
        } else {
            return ResponseEntity.status(401).body("인증 실패.");
        }
    }

    // 3. 회원가입 완료 (파일 첨부 포함)
    @PostMapping("/register")
    public ResponseEntity<Integer> register(
        @RequestPart RegisterRequest request,
        @RequestPart(required = false) MultipartFile file
    ) {
        if (file != null) {
            UploadFile uploadFile = storageService.storeFile(file);
            Integer memberId = loginService.register(
                request.getEmail(),
                request.getPassword(),
                request.getPassword(),
                uploadFile.getStoreFilename()
            );
            return ResponseEntity.ok(memberId);
        } else {
            Integer memberId = loginService.register(
                request.getEmail(),
                request.getPassword(),
                request.getPassword()
            );
            return ResponseEntity.ok(memberId);
        }
    }

    // 4. 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request.getEmail(), request.getPassword()));
    }
}
