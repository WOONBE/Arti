package com.d106.arti.member.service;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.domain.Theme;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.gallery.repository.ThemeRepository;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMemberService {

    private final MemberRepository memberRepository;
    private final GalleryRepository galleryRepository;
    private final ThemeRepository themeRepository;

    @Transactional
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
