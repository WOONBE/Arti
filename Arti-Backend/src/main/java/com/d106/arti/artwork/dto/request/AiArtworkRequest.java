package com.d106.arti.artwork.dto.request;

import com.d106.arti.artwork.domain.AiArtwork;
import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.gallery.Theme;
import com.d106.arti.member.domain.Member;
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

    public AiArtwork toAiArtwork(Artwork originalImage, Member member, Theme theme) {
        return AiArtwork.builder()
            .aiArtworkTitle(aiArtworkTitle)
            .artworkImage(artworkImage)
            .originalImage(originalImage)
            .genre(genre)
            .member(member)
            .theme(theme)
            .build();
    }
}