package com.d106.arti.artwork.dto.response;

import com.d106.arti.artwork.domain.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponse {
    private Integer artist_id;
    private String engName;
    private String korName;
    private String image;
    private String summary;

    public static ArtistResponse toArtistResponse(Artist artist) {
        return ArtistResponse.builder()
            .artist_id(artist.getId())
            .engName(artist.getEngName())
            .korName(artist.getKorName())
            .image(artist.getImage())
            .summary(artist.getSummary())
            .build();
    }
}


