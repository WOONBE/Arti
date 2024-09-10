package com.d106.arti.artwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AiArtworkRequest {
    private String aiArtworkTitle;
    private String artworkImage;
    private String genre;
    private Integer memberId;  // 사용자 ID
    private Integer themeId;   // 테마 ID
    private Integer originalImageId; // 기존 artwork ID
}
