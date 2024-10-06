package com.d106.arti.member.service;

import static com.d106.arti.global.exception.ExceptionCode.ALREADY_SUBSCRIBED_GALLERY;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_GALLERY_ID;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.d106.arti.global.exception.ExceptionCode.NOT_SUBSCRIBED_GALLERY;

import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GalleryRepository galleryRepository; // Gallery 조회를 위해 사용

    //    // 미술관 구독 기능
//    @Transactional
//    public String subscribeGallery(Integer memberId, Integer galleryId) {
//        //token을 통해 받은 본인 id와 같으면 구독 불가하게 막기
//
//        // 1. 회원 정보 조회
//        Member member = memberRepository.findById(memberId)
//            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
//
//        // 2. 미술관이 존재하는지 확인
//        if (!galleryRepository.existsById(galleryId)) {
//            throw new BadRequestException(NOT_FOUND_GALLERY_ID);
//        }
//
//        // 3. 이미 구독한 미술관인지 확인 후 구독 처리
//        if (!member.getSubscribedGalleryIds().contains(galleryId)) {
//            member.getSubscribedGalleryIds().add(galleryId);
//            memberRepository.save(member); // 변경 사항 저장
//            return "미술관 구독이 완료되었습니다.";
//        } else {
//            throw new BadRequestException(ALREADY_SUBSCRIBED_GALLERY);
//        }
//    }
//
//    // 미술관 구독 취소 기능
//    @Transactional
//    public String  unsubscribeGallery(Integer memberId, Integer galleryId) {
//        // 1. 회원 정보 조회
//        Member member = memberRepository.findById(memberId)
//            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
//
//        // 2. 구독 목록에서 해당 미술관 ID를 제거
//        if (member.getSubscribedGalleryIds().contains(galleryId)) {
//            member.getSubscribedGalleryIds().remove(galleryId);
//            memberRepository.save(member); // 변경 사항 저장
//            return "미술관 구독 취소가 완료되었습니다.";
//        } else {
//            throw new BadRequestException(NOT_SUBSCRIBED_GALLERY);
//        }
//    }
    @Transactional
    public Map<String, Object> subscribeGallery(Integer memberId, Integer galleryId) {
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

            Map<String, Object> response = new HashMap<>();
            response.put("message", "미술관 구독이 완료되었습니다.");
            return response;
        } else {
            throw new BadRequestException(ALREADY_SUBSCRIBED_GALLERY);
        }
    }

    @Transactional
    public Map<String, Object> unsubscribeGallery(Integer memberId, Integer galleryId) {
        // 1. 회원 정보 조회ue
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        // 2. 구독 목록에서 해당 미술관 ID를 제거
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