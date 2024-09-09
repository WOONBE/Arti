package com.d106.arti.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiArtWorkResponseDto {
    private Integer id;
    private String aiArtworkTitle;
    private String artworkImage;
    private String genre;
    private Integer memberId;
    private Integer themeId;
    private Integer originalImageId;
}
