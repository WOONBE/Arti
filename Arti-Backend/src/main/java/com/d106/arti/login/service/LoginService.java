package com.d106.arti.login.service;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final GalleryRepository galleryRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Integer register(String email, String password, String nickname) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bcryptPasswordEncoder.encode(password);
        Member member = Member.builder().email(email).password(encodedPassword).nickname(nickname)
            .build();
        return memberRepository.save(member).getId();
    }

    public LoginResponse login(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));
        Member member = memberRepository.findByEmail(email).orElseThrow();
        Optional<Gallery> gallery = galleryRepository.findFirstByOwner(member);
        String token = jwtService.generateToken(member);
        return LoginResponse.builder().token(token).expiresIn(jwtService.getExpirationTime())
            .memberId(member.getId()).galleryId(gallery.isPresent() ? gallery.get().getId() : -1)
            .build();
    }
}
