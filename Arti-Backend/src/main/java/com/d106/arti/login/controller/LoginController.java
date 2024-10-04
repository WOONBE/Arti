package com.d106.arti.login.controller;

import com.d106.arti.login.dto.request.VerificationNumberRequest;
import com.d106.arti.login.dto.request.VerificationRequest;
import com.d106.arti.login.service.JwtService;
import com.d106.arti.login.service.LoginRequest;
import com.d106.arti.login.service.LoginResponse;
import com.d106.arti.login.service.LoginService;
import com.d106.arti.login.service.MailService;
import com.d106.arti.login.service.RegisterRequest;
import com.d106.arti.login.service.VerificationService;
import com.d106.arti.member.repository.MemberRepository;
import com.d106.arti.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final JwtService jwtService;
    private final LoginService loginService;
    private final StorageService storageService;
    private final MailService mailService;
    private final VerificationService verificationService;
    private final MemberRepository memberRepository;

    // 1. 회원가입 요청 (이메일 인증번호 전송)
    @PostMapping("/request-auth-number")
    @Operation(summary = "이메일 인증번호 전송", description = "이메일 인증번호 전송을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드가 전송되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "409", description = "이미 등록된 이메일입니다.")
    })
    public ResponseEntity<?> requestSignUp(@RequestBody VerificationNumberRequest verificationNumberRequest) {
        String email = verificationNumberRequest.getEmail();

        if (memberRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body("이미 등록된 이메일입니다.");
        }

        String code = mailService.sendMail(email);
        verificationService.saveVerificationCode(email, code);
        return ResponseEntity.ok("인증 코드가 전송되었습니다. 이메일: " + email);
    }

    // 2. 이메일 인증 (인증번호 확인)
    @PostMapping("/verify-code")
    @Operation(summary = "이메일 인증번호 확인", description = "이메일 인증번호 확인을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공, 회원가입을 계속 진행하세요."),
            @ApiResponse(responseCode = "401", description = "인증 실패.")
    })
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        boolean isValid = verificationService.verifyCode(verificationRequest.getEmail(),
                verificationRequest.getCode());

        if (isValid) {
            return ResponseEntity.ok("인증 성공, 회원가입을 계속 진행하세요.");
        } else {
            return ResponseEntity.status(401).body("인증 실패.");
        }
    }
    // 3. 회원가입 완료 (파일 첨부 포함)
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "인증된 이메일을 통한 회원가입을 위한 API")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (!verificationService.isVerifiedEmail(request.getEmail())) {
            return ResponseEntity.status(401).body("이메일 인증이 필요합니다.");
        }


        Integer memberId = loginService.register(
                request.getEmail(),
                request.getPassword(),
                request.getNickname()
        );


        return ResponseEntity.ok(memberId);
    }

    // 4. 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 위한 API")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request.getEmail(), request.getPassword()));
    }
}
