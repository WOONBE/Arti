package com.d106.arti.artwork.service;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.repository.ArtistRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Async;
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
//
//    public List<Artist> searchBySummary(String summary) {
//        return artistRepository.findBySummaryContaining(summary);
//    }

    // 중복 제거를 포함한 통합 검색 메서드
    public List<Artist> search(String keyword) {
        Set<Artist> resultSet = new HashSet<>(); // 중복을 제거하기 위해 Set 사용

        if (keyword != null && !keyword.isEmpty()) {
            resultSet.addAll(searchByEngName(keyword));
            resultSet.addAll(searchByKorName(keyword));
        }
        List<Artist> resultList = resultSet.stream().collect(Collectors.toList());

        // 결과가 비어 있으면 예외 발생
        if (resultList.isEmpty()) {
            throw new RuntimeException("화가 정보가 없습니다.");
        }

        return resultList;
    }



}
