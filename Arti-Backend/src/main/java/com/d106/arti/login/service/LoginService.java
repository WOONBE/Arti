package com.d106.arti.login.service;

import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Member register(String email, String password, String nickname) {
        Member member = Member.builder().email(email).password(passwordEncoder.encode(password))
            .nickname(nickname).build();
        return memberRepository.save(member);
    }

    public LoginResponse login(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                email,
                password
            )
        );
        Member member = memberRepository.findByEmail(email).orElseThrow();
        String token = jwtService.generateToken(member);
        return LoginResponse.builder()
            .token(token)
            .expiresIn(jwtService.getExpirationTime())
            .build();
    }
}
