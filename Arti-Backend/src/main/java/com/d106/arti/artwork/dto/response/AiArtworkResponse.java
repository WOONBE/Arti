package com.d106.arti.artwork.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AiArtworkResponse {
    private Integer id;
    private String aiArtworkTitle;
    private String artworkImage;
    private String genre;
    private Integer memberId;
    private Integer themeId;
    private Integer originalImageId;
}
