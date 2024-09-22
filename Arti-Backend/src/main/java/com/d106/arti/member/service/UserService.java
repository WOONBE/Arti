package com.d106.arti.member.service;

import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;
// 접속한 유저가 구독한 미술관 조회를 위해
    // 현재 인증된 사용자를 반환하는 메서드
    public Member getCurrentUser() {
        // SecurityContext에서 인증된 사용자 정보 가져오기
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 이메일을 기반으로 사용자를 조회
        return memberRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }
}
