package com.d106.arti.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeRequest {
    private String themeTitle;
    private Integer galleryId;  // 갤러리 ID (해당 테마가 속한 갤러리)
}
