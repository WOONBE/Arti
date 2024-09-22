package com.d106.arti.gallery.dto.response;

import com.d106.arti.gallery.domain.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private Integer galleryId;
    private String galleryTitle;
    private boolean isActive; // 구독 활성화 상태

    public static SubscriptionResponse fromEntity(Subscription subscription) {
        return SubscriptionResponse.builder()
                .galleryId(subscription.getGallery().getId())
                .galleryTitle(subscription.getGallery().getGalleryTitle())
                .isActive(subscription.isActive()) // 구독 활성화 상태 포함
                .build();
    }
}
