package com.d106.arti.artwork.service;

import static com.d106.arti.global.exception.ExceptionCode.INVALID_GENRE;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_ARTIST;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.dto.response.ArtistResponse;
import com.d106.arti.artwork.repository.ArtistRepository;
import com.d106.arti.artwork.repository.ArtworkRepository;
import com.d106.arti.gallery.domain.Genre;
import com.d106.arti.global.exception.BadRequestException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtworkRepository artworkRepository;

    @Transactional(readOnly = true)
    public List<ArtistResponse> searchByEngName(String engName) {
        return artistRepository.findByEngNameContaining(engName)
            .stream()
            .map(ArtistResponse::toArtistResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ArtistResponse> searchByKorName(String korName) {
        return artistRepository.findByKorNameContaining(korName)
            .stream()
            .map(ArtistResponse::toArtistResponse)
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "searchArtists", key = "#keyword", sync = true, cacheManager = "rcm")
    public List<ArtistResponse> search(String keyword) {
        Set<Artist> resultSet = new HashSet<>();

        if (keyword != null && !keyword.isEmpty()) {
            resultSet.addAll(artistRepository.findByEngNameContaining(keyword));
            resultSet.addAll(artistRepository.findByKorNameContaining(keyword));
        }

        List<ArtistResponse> resultList = resultSet.stream()
            .map(ArtistResponse::toArtistResponse)
            .collect(Collectors.toList());

        if (resultList.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ARTIST);
        }

        return resultList;
    }
    // ID로 단건 조회
    @Transactional(readOnly = true)
    public ArtistResponse findArtistById(Integer artistId) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTIST));
        return ArtistResponse.toArtistResponse(artist);
    }

    // 캐시를 적용하여 50명의 화가를 랜덤하게 가져오는 메서드
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "randomArtists", key = "#root.target + #root.methodName", sync = true, cacheManager = "rcm")
    public List<ArtistResponse> getRandomArtists() {
        // 모든 화가를 가져온 후 랜덤하게 섞는다
        List<Artist> allArtists = artistRepository.findAll();

        // 화가가 50명 미만일 경우 처리
        if (allArtists.size() < 50) {
            throw new BadRequestException(NOT_FOUND_ARTIST);
        }

        // 리스트를 섞는다
        Collections.shuffle(allArtists);

        // 50명의 화가를 선택하고 ArtistResponse로 변환
        return allArtists.stream()
            .limit(50)
            .map(ArtistResponse::toArtistResponse)
            .collect(Collectors.toList());
    }

    // 장르로 검색된 미술품들의 화가를 중복 없이 3명만 반환하는 메서드
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "genreArtists", key = "#genreLabel", sync = true, cacheManager = "rcm")
    public List<ArtistResponse> getArtistsByGenre(String genreLabel) {

        String formattedGenreLabel = genreLabel.trim().toUpperCase();

        Genre genre;
        try {
            genre = Genre.valueOf(formattedGenreLabel); // Enum 값이 존재하는지 확인
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(INVALID_GENRE); // 유효하지 않은 장르일 경우 예외 처리
        }


        String genreLabelForSearch = formattedGenreLabel.replace("_", " ");


        List<NormalArtWork> artworks = artworkRepository.findAllByGenreContaining(genreLabelForSearch);


        Set<Artist> uniqueArtists = new HashSet<>();
        for (NormalArtWork artwork : artworks) {
            if (uniqueArtists.size() < 3) {
                uniqueArtists.add(artwork.getArtist());
            } else {
                break;
            }
        }

        return uniqueArtists.stream()
                .map(ArtistResponse::toArtistResponse)
                .collect(Collectors.toList());
    }






}
