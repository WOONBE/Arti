package com.d106.arti.gallery.service;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.dto.request.GalleryRequest;
import com.d106.arti.gallery.dto.response.GalleryResponse;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.member.domain.Member; // 추가
import com.d106.arti.member.repository.MemberRepository; // 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final MemberRepository memberRepository; // 추가

    // 갤러리 타이틀 수정 (기존에 있던 로직)
    @Transactional
    public void updateGalleryTitle(Integer galleryId, String newTitle) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 갤러리를 찾을 수 없습니다."));
        gallery.setGalleryTitle(newTitle);
        galleryRepository.save(gallery);
    }

    // 갤러리 업데이트 (타이틀, 설명, 이미지 수정)
    @Transactional
    public GalleryResponse updateGallery(Integer galleryId, GalleryRequest request) {
        // 갤러리 조회
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 갤러리를 찾을 수 없습니다."));

        // 갤러리 정보 수정
        gallery.setGalleryTitle(request.getGalleryTitle());
        gallery.setDescription(request.getDescription());
        gallery.setImage(request.getImage());

        // 갤러리 저장
        galleryRepository.save(gallery);

        // 수정된 갤러리 정보를 반환
        return GalleryResponse.fromEntity(gallery);
    }

    // 특정 회원의 미술관 조회 (수정됨: memberId로 Member 조회)
    public GalleryResponse getGalleryByMember(Integer memberId) {
        // memberId로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        // 해당 회원의 갤러리 조회
        Gallery gallery = galleryRepository.findByOwner(member)
                .stream().findFirst() // 가정: 회원 당 하나의 갤러리
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 미술관을 찾을 수 없습니다."));

        return GalleryResponse.fromEntity(gallery);
    }
}
