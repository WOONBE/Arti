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
public class SubscribedGalleryResponse {
    private Integer galleryId;         // 미술관 ID
    private String galleryName;        // 미술관 이름
    private String galleryDescription; // 미술관 설명
    private String galleryImage;       // 미술관 이미지
    private Integer viewCount;         // 미술관 조회수
    private String ownerName;          // 미술관 소유자 이름

    // Entity에서 DTO로 변환하는 메서드
    public static SubscribedGalleryResponse fromEntity(Gallery gallery) {
        return SubscribedGalleryResponse.builder()
                .galleryId(gallery.getId())
                .galleryName(gallery.getName())
                .galleryDescription(gallery.getDescription())
                .galleryImage(gallery.getImage())
                .viewCount(gallery.getView())
                .ownerName(gallery.getOwner().getNickname())  // 소유자 이름
                .build();
    }
}
