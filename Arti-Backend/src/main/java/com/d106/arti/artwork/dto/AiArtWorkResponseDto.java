package com.d106.arti.artwork.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiArtWorkResponseDto {
    private Integer id;
    private String aiArtworkTitle;
    private String artworkImage;
    private String genre;
    private Integer memberId;
    private Integer themeId;
    private Integer originalImageId;
}
