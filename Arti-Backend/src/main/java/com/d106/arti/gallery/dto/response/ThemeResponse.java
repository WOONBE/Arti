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
    private Integer themeId;
    private String themeTitle;
    private Integer galleryId;

    public static ThemeResponse fromEntity(Theme theme) {
        return ThemeResponse.builder()
                .themeId(theme.getId())
                .themeTitle(theme.getThemeTitle())
                .galleryId(theme.getGallery().getId())
                .build();
    }
}
