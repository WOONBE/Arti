package com.d106.arti.artwork.service;

import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_ARTWORK;

import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.dto.response.NormalArtworkResponse;
import com.d106.arti.artwork.repository.ArtworkRepository;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.global.exception.ExceptionCode;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtworkService {


    private final ArtworkRepository artworkRepository;

    @Value("${server.image.base-url}")
    private String imageBaseUrl;

//    // 검색 메서드
//    @Transactional(readOnly = true)
//    public List<NormalArtworkResponse> searchArtworks(String keyword) {
//        List<NormalArtWork> artworks = artworkRepository.search(keyword);
//
//        if (artworks.isEmpty()) {
//            throw new BadRequestException(NOT_FOUND_ARTWORK);
//        }
//
//        return artworks.stream()
//            .map(artwork -> NormalArtworkResponse.fromEntity(artwork, imageBaseUrl))
//            .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public Page<NormalArtworkResponse> searchArtworks(String keyword, int page) {
        // 페이지당 30개씩 처리하도록 Pageable 설정 (페이지 번호는 0부터 시작하므로 -1 처리)
        Pageable pageable = PageRequest.of(page - 1, 30);

        // 페이징 처리된 검색 결과를 가져옴
        Page<NormalArtWork> artworksPage = artworkRepository.search(keyword, pageable);

        // 검색 결과가 비어있으면 예외 처리
        if (artworksPage.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ARTWORK);
        }

        // 검색 결과를 NormalArtworkResponse로 변환하여 반환
        return artworksPage.map(artwork -> NormalArtworkResponse.fromEntity(artwork, imageBaseUrl));
    }

    // 단건 조회 메서드
    @Transactional(readOnly = true)
    public NormalArtworkResponse getArtworkById(Integer id) {
        NormalArtWork artwork = artworkRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTWORK));

        return NormalArtworkResponse.fromEntity(artwork, imageBaseUrl);
    }



}
