package com.d106.arti.artwork.dto.response;

import com.d106.arti.artwork.domain.AiArtwork;
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

    public static AiArtworkResponse toAiArtworkResponse(AiArtwork aiArtwork) {
        return AiArtworkResponse.builder()
            .id(aiArtwork.getId())
            .aiArtworkTitle(aiArtwork.getAiArtworkTitle())
            .artworkImage(aiArtwork.getArtworkImage())
            .genre(aiArtwork.getGenre())
            .memberId(aiArtwork.getMember() != null ? aiArtwork.getMember().getId() : null)
            .themeId(aiArtwork.getTheme() != null ? aiArtwork.getTheme().getId() : null)
            .originalImageId(aiArtwork.getOriginalImage() != null ? aiArtwork.getOriginalImage().getId() : null)
            .build();
    }
}
