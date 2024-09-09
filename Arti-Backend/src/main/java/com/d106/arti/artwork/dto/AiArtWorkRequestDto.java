package com.d106.arti.artwork.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiArtWorkRequestDto {
    private String aiArtworkTitle;
    private String artworkImage;
    private String genre;
    private Integer memberId;  // 사용자 ID
    private Integer themeId;   // 테마 ID
    private Integer originalImageId; // 기존 artwork ID
}
