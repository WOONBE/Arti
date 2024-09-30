package com.d106.arti.artwork.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistRequest {
    private String engName;
    private String korName;
    private String keyword;  // 통합 검색에 사용될 필드
}
