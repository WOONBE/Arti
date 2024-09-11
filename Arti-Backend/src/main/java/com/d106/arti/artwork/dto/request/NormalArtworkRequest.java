package com.d106.arti.artwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NormalArtworkRequest {
    private String filename;
    private String artist;
    private String genre;
    private String description;
    private String phash;
    private Integer width;
    private Integer height;
    private Integer genreCount;
    private String subset;
    private String artistKo;
    private String title;
    private String year;
}
