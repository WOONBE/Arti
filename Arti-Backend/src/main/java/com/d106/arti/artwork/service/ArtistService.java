package com.d106.arti.artwork.service;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> searchByEngName(String engName) {
        return artistRepository.findByEngNameContaining(engName);
    }

    public List<Artist> searchByKorName(String korName) {
        return artistRepository.findByKorNameContaining(korName);
    }

    public List<Artist> searchBySummary(String summary) {
        return artistRepository.findBySummaryContaining(summary);
    }
}
