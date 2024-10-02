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
    private String name;         // 테마 이름
    private Integer galleryId;   // 갤러리 ID
}