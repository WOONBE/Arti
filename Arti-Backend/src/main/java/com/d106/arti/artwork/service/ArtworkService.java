package com.d106.arti.artwork.service;

import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_ARTIST;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_ARTWORK;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.dto.response.ArtistResponse;
import com.d106.arti.artwork.dto.response.ArtworkResponse;
import com.d106.arti.artwork.dto.response.NormalArtworkResponse;
import com.d106.arti.artwork.repository.ArtworkRepository;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.global.exception.ExceptionCode;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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


    @Transactional(readOnly = true)
//    @Cacheable(cacheNames = "searchArtworks", key = "#keyword + '_' + #page", sync = true, cacheManager = "rcm")
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

    // 캐시를 적용하여 50개의 미술품을 랜덤하게 가져오는 메서드
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "ramdomArtworks", key = "#root.target + #root.methodName", sync = true, cacheManager = "rcm")
    public List<ArtworkResponse> getRandomArtworks() {
        // 모든 미술품을 가져온 후 랜덤하게 섞는다
        List<NormalArtWork> artworks = artworkRepository.findAll();

        // 미술품이 50개 미만일 경우 처리
        if (artworks.size() < 50) {
            throw new BadRequestException(NOT_FOUND_ARTWORK);
        }

        // 리스트를 섞는다
        Collections.shuffle(artworks);

        // 50개의 미술품을 선택하고 ArtworkResponse로 변환
        return artworks.stream()
            .limit(50)
            .map(artwork -> ArtworkResponse.builder()
                .id(artwork.getId())
                .title(artwork.getTitle())
                .description(artwork.getDescription())
                .imageUrl(imageBaseUrl+artwork.getFilename())
                .artist(artwork.getArtist().getEngName())  // 화가 정보 가져오기
                .year(artwork.getYear())
                .build())
            .collect(Collectors.toList());
    }






}
