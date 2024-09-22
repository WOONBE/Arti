package com.d106.arti.gallery.service;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.dto.response.GalleryResponse;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;

    // 갤러리 타이틀 수정 (수정됨)
    @Transactional
    public void updateGalleryTitle(Integer galleryId, String newTitle) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 갤러리를 찾을 수 없습니다."));
        gallery.setGalleryTitle(newTitle); // 타이틀 수정 메서드 서비스로 이동
    }

    // 특정 회원의 미술관 조회
    public GalleryResponse getGalleryByMember(Member member) {
        Gallery gallery = galleryRepository.findByOwner(member)
                .stream().findFirst() // 가정: 회원 당 하나의 갤러리
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 미술관을 찾을 수 없습니다."));
        return GalleryResponse.fromEntity(gallery);
    }
}
