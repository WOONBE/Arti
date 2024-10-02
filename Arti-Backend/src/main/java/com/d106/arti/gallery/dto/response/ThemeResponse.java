package com.d106.arti.gallery.dto.response;

import com.d106.arti.gallery.domain.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeResponse {
    private Integer id;          // 테마 ID
    private String name;         // 테마 이름
    private String galleryName;  // 갤러리 이름

    // Theme 엔티티를 DTO로 변환하는 메서드
    public static ThemeResponse fromEntity(Theme theme) {
        return ThemeResponse.builder()
            .id(theme.getId())
            .name(theme.getName())
            .galleryName(theme.getGallery().getName())
            .build();
    }
}