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
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtworkService {


    private final ArtworkRepository artworkRepository;


    // 검색 메서드
    @Transactional(readOnly = true)
    public List<NormalArtworkResponse> searchArtworks(String keyword) {
        List<NormalArtWork> artworks = artworkRepository.search(keyword);

        if (artworks.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ARTWORK);
        }

        return artworks.stream()
            .map(NormalArtworkResponse::fromEntity)
            .collect(Collectors.toList());
    }



}
