package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Integer> {

    // 갤러리를 기준으로 테마 조회
    Optional<Theme> findByGallery(Gallery gallery);

    // 해당 갤러리의 테마 개수를 확인하는 메서드 => 각 계정마다 최소 테마 1개를 유지해야함.
    long countByGallery(Gallery gallery);

    // 테마 이름을 기준으로 테마 목록 조회 (테마 이름이 정확히 일치하는 경우)
    List<Theme> findByThemeTitle(String themeTitle);

    // 테마 이름을 부분 검색(포함된 이름)으로 조회 (테마 이름에 특정 단어가 포함된 경우)
    List<Theme> findByThemeTitleContainingIgnoreCase(String keyword);
}
