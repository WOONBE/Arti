package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Theme 엔티티와 관련된 데이터베이스 접근 메서드를 정의하는 인터페이스
public interface ThemeRepository extends JpaRepository<Theme, Integer> {

    // 특정 갤러리에 속한 테마를 조회
    Optional<Theme> findByGallery(Gallery gallery);

    // 해당 갤러리의 테마 개수를 확인하는 메서드
    long countByGallery(Gallery gallery);

    // 테마 이름을 기준으로 테마 목록 조회 (정확한 일치)
    List<Theme> findByThemeTitle(String themeTitle);

    // 테마 이름을 포함 검색으로 조회 (대소문자 무시)
    List<Theme> findByThemeTitleContainingIgnoreCase(String keyword);
}
