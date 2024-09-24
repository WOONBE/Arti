package com.d106.arti.login.service;

import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Integer register(String email, String password, String nickname) {
        Member member = Member.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
        return memberRepository.save(member).getId();
    }

    public Integer register(String email, String password, String nickname, String image) {
        Member member = Member.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .image(image)
            .build();
        return memberRepository.save(member).getId();
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
