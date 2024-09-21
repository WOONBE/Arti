package com.d106.arti.gallery.service;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.domain.Theme;
import com.d106.arti.gallery.dto.request.ThemeRequest;
import com.d106.arti.gallery.dto.response.ThemeResponse;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.gallery.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private GalleryRepository galleryRepository;

    // 특정 갤러리의 테마 전체 조회
    @Transactional(readOnly = true)
    public List<ThemeResponse> getThemesByGalleryId(Integer galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 갤러리를 찾을 수 없습니다."));

        return themeRepository.findByGallery(gallery)
                .stream()
                .map(ThemeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 테마 생성
    @Transactional
    public ThemeResponse createTheme(Integer galleryId, ThemeRequest request) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 갤러리를 찾을 수 없습니다."));

        Theme newTheme = Theme.builder()
                .gallery(gallery)
                .themeTitle(request.getThemeTitle())
                .build();

        Theme savedTheme = themeRepository.save(newTheme);
        return ThemeResponse.fromEntity(savedTheme);
    }

    // 테마 이름 수정
    @Transactional
    public ThemeResponse updateThemeTitle(Integer themeId, String newTitle) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마를 찾을 수 없습니다."));

        theme.setThemeTitle(newTitle);
        Theme updatedTheme = themeRepository.save(theme);
        return ThemeResponse.fromEntity(updatedTheme);
    }

    // 테마 삭제 (하나 남겨두기)
    @Transactional
    public void deleteTheme(Integer themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마를 찾을 수 없습니다."));

        // 해당 갤러리의 테마가 하나 남았는지 확인
        long themeCount = themeRepository.countByGallery(theme.getGallery());
        if (themeCount <= 1) {
            throw new IllegalStateException("하나의 테마는 반드시 남아 있어야 합니다.");
        }

        themeRepository.delete(theme);
    }





    // 테마 수정 (요청에 따라 테마 제목을 수정하는 메서드)
    @Transactional
    public ThemeResponse updateTheme(Integer themeId, ThemeRequest request) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마를 찾을 수 없습니다."));

        theme.setThemeTitle(request.getThemeTitle());
        Theme updatedTheme = themeRepository.save(theme);
        return ThemeResponse.fromEntity(updatedTheme);
    }
}
