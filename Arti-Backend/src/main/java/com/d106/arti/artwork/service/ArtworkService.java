package com.d106.arti.artwork.service;

import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.repository.ArtworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkService {


    private final ArtworkRepository artworkRepository;

    //검색 메서드
    public List<NormalArtWork> searchArtworks(String keyword) {
        return artworkRepository.search(keyword);
    }



}
