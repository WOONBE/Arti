package com.d106.arti.artwork.service;

import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_ARTIST;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.dto.response.ArtistResponse;
import com.d106.arti.artwork.repository.ArtistRepository;
import com.d106.arti.global.exception.BadRequestException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

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







}
