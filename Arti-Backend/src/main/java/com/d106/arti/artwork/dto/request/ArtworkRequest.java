package com.d106.arti.artwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkRequest {
    private Integer artworkId;  // 미술품 ID
    private String description; // 미술품 설명
}