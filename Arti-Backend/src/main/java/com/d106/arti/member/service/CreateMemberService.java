package com.d106.arti.member.service;

import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMemberService {

    private final MemberRepository memberRepository;

    public void createMember(
        final String email,
        final String password,
        final String nickname,
        final String image
    ) {
        Member member = Member.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .image(image)
            .build();
        memberRepository.save(member);
    }
}
