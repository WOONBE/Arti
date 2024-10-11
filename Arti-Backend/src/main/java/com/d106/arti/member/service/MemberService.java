package com.d106.arti.member.service;

import static com.d106.arti.global.exception.ExceptionCode.ALREADY_SUBSCRIBED_GALLERY;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_GALLERY_ID;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.d106.arti.global.exception.ExceptionCode.NOT_SUBSCRIBED_GALLERY;

import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.member.controller.ChangeNicknameRequest;
import com.d106.arti.member.controller.ChangePasswordRequest;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String getNickname(Principal connectedUser) {
        Member member = (Member) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return member.getNickname();
    }

    @Transactional
//    @CacheEvict(cacheNames = "subGalleriesByMemberId", key = "#memberId", cacheManager = "rcm")
    public Map<String, Object> subscribeGallery(Integer memberId, Integer galleryId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));


        // 1. 회원 정보 조회
        if (!member.getSubscribedGalleryIds().contains(galleryId)) {
            member.getSubscribedGalleryIds().add(galleryId);
            memberRepository.save(member); // 변경 사항 저장

            Map<String, Object> response = new HashMap<>();
            response.put("message", "미술관 구독이 완료되었습니다.");
            return response;
        } else {
            throw new BadRequestException(ALREADY_SUBSCRIBED_GALLERY);
        }
    }

    @Transactional
//    @CacheEvict(cacheNames = "subGalleriesByMemberId", key = "#memberId", cacheManager = "rcm")
    public Map<String, Object> unsubscribeGallery(Integer memberId, Integer galleryId) {
        // 1. 회원 정보 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        if (member.getSubscribedGalleryIds().contains(galleryId)) {
            member.getSubscribedGalleryIds().remove(galleryId);
            memberRepository.save(member); // 변경 사항 저장

            Map<String, Object> response = new HashMap<>();
            response.put("message", "미술관 구독 취소가 완료되었습니다.");
            return response;
        } else {
            throw new BadRequestException(NOT_SUBSCRIBED_GALLERY);
        }
    }




}
