package com.d106.arti.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryRequest {
    private String galleryTitle;
    private String description;
    private String image;
    private Integer memberId; // 갤러리를 소유하는 회원 ID
}
