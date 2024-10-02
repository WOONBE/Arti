package com.d106.arti.artwork.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkResponse {
    private Integer id;          // 미술품 ID
    private String title;        // 미술품 제목
    private String description;  // 미술품 설명
    private String imageUrl;
    private String artist;
    private String year;
}