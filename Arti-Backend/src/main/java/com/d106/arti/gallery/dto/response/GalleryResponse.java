package com.d106.arti.gallery.dto.response;

import com.d106.arti.gallery.domain.Gallery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryResponse {
    private Integer id;
    private String galleryTitle;
    private String description;
    private String image;
    private String ownerName; // 미술관 소유자 이름

    // Gallery 객체를 받아 GalleryResponse 객체로 변환
    public static GalleryResponse fromEntity(Gallery gallery) {
        return GalleryResponse.builder()
                .id(gallery.getId())
                .galleryTitle(gallery.getGalleryTitle())
                .description(gallery.getDescription())
                .image(gallery.getImage())
                .ownerName(gallery.getOwner().getNickname())
                .build();
    }
}
