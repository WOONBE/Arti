package com.d106.arti.login.controller;

import com.d106.arti.login.service.JwtService;
import com.d106.arti.login.service.LoginRequest;
import com.d106.arti.login.service.LoginResponse;
import com.d106.arti.login.service.LoginService;
import com.d106.arti.login.service.RegisterRequest;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request.getEmail(), request.getPassword()));
    }
}
