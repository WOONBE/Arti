package com.d106.arti.member.service;

import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.member.controller.ChangeNicknameRequest;
import com.d106.arti.member.controller.ChangePasswordRequest;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static com.d106.arti.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final GalleryRepository galleryRepository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        memberRepository.save(member);
    }

    public void changeNickname(ChangeNicknameRequest request, Principal connectedUser) {
        Member member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        member.setNickname(request.getNewNickname());
        memberRepository.save(member);
    }

    // 미술관 구독 기능
    @Transactional
    public String subscribeGallery(Integer memberId, Integer galleryId) {
        //token을 통해 받은 본인 id와 같으면 구독 불가하게 막기

        // 1. 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        // 2. 미술관이 존재하는지 확인
        if (!galleryRepository.existsById(galleryId)) {
            throw new BadRequestException(NOT_FOUND_GALLERY_ID);
        }

        // 3. 이미 구독한 미술관인지 확인 후 구독 처리
        if (!member.getSubscribedGalleryIds().contains(galleryId)) {
            member.getSubscribedGalleryIds().add(galleryId);
            memberRepository.save(member); // 변경 사항 저장
            return "미술관 구독이 완료되었습니다.";
        } else {
            throw new BadRequestException(ALREADY_SUBSCRIBED_GALLERY);
        }
    }

    // 미술관 구독 취소 기능
    @Transactional
    public String unsubscribeGallery(Integer memberId, Integer galleryId) {
        // 1. 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        // 2. 구독 목록에서 해당 미술관 ID를 제거
        if (member.getSubscribedGalleryIds().contains(galleryId)) {
            member.getSubscribedGalleryIds().remove(galleryId);
            memberRepository.save(member); // 변경 사항 저장
            return "미술관 구독 취소가 완료되었습니다.";
        } else {
            throw new BadRequestException(NOT_SUBSCRIBED_GALLERY);
        }
    }
}
