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
        // 1. 새로운 회원 생성
        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .image(image)
                .build();

        memberRepository.save(member);

        // 2. 해당 회원을 위한 갤러리 생성
        Gallery gallery = Gallery.builder()
                .owner(member)
                .galleryTitle(nickname + "의 미술관")  // 기본 갤러리 이름은 닉네임 기반
                .build();

        galleryRepository.save(gallery);

        // 3. 기본 테마 생성
        Theme defaultTheme = Theme.builder()
                .gallery(gallery)
                .themeTitle(nickname + "의 메인 테마")  // 기본 테마 이름은 닉네임 기반
                .build();

        themeRepository.save(defaultTheme);
    }

    // 회원 탈퇴 시 갤러리와 테마까지 전부 삭제하는 메소드
    @Transactional
    public void deleteMember(Member member) {
        galleryRepository.deleteByOwner(member); // 회원 소유의 모든 갤러리 삭제
        memberRepository.delete(member); // 회원 삭제
    }
}
