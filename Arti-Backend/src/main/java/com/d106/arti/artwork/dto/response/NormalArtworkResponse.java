package com.d106.arti.artwork.dto.response;

import com.d106.arti.artwork.domain.NormalArtWork;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NormalArtworkResponse {
    private Integer artwork_id;
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


//    public static NormalArtworkResponse fromEntity(NormalArtWork normalArtWork) {
//        return NormalArtworkResponse.builder()
//            .artwork_id(normalArtWork.getId())
//            .filename(normalArtWork.getFilename())
//            .artist(normalArtWork.getArtist())
//            .genre(normalArtWork.getGenre())
//            .description(normalArtWork.getDescription())
//            .phash(normalArtWork.getPhash())
//            .width(normalArtWork.getWidth())
//            .height(normalArtWork.getHeight())
//            .genreCount(normalArtWork.getGenreCount())
//            .subset(normalArtWork.getSubset())
//            .artistKo(normalArtWork.getArtistKo())
//            .title(normalArtWork.getTitle())
//            .year(normalArtWork.getYear())
//            .build();
//    }
    public static NormalArtworkResponse fromEntity(NormalArtWork normalArtWork, String baseUrl) {
        String imagePath = normalArtWork.getFilename();
        String imageUrl = baseUrl + imagePath;

        return NormalArtworkResponse.builder()
            .artwork_id(normalArtWork.getId())
            .filename(imageUrl) // EC2 이미지 경로 적용
            .artist(normalArtWork.getArtistName())
            .genre(normalArtWork.getGenre())
            .description(normalArtWork.getDescription())
            .phash(normalArtWork.getPhash())
            .width(normalArtWork.getWidth())
            .height(normalArtWork.getHeight())
            .genreCount(normalArtWork.getGenreCount())
            .subset(normalArtWork.getSubset())
            .artistKo(normalArtWork.getArtistKo())
            .title(normalArtWork.getTitle())
            .year(normalArtWork.getYear())
            .build();
    }
}
