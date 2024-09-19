package com.d106.arti.login.controller;

import com.d106.arti.login.service.JwtService;
import com.d106.arti.login.service.LoginRequest;
import com.d106.arti.login.service.LoginResponse;
import com.d106.arti.login.service.LoginService;
import com.d106.arti.login.service.RegisterRequest;
import com.d106.arti.member.domain.Member;
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

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        Member member = loginService.register(request.getEmail(), request.getPassword(),
            request.getNickname());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request.getEmail(), request.getPassword()));
    }
}
