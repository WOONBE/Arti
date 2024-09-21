package com.d106.arti.gallery.service;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    // 갤러리 이름 수정
    @Transactional
    public Gallery updateGalleryTitle(Integer galleryId, String newTitle) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 갤러리를 찾을 수 없습니다."));

        gallery.setGalleryTitle(newTitle);
        return galleryRepository.save(gallery);
    }
}