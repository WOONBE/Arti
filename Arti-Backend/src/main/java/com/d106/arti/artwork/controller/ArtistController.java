package com.d106.arti.artwork.controller;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.dto.response.ArtistResponse;
import com.d106.arti.artwork.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    // 영어 이름으로 검색
    @GetMapping("/search/artist/eng-name")
    public ResponseEntity<List<ArtistResponse>> searchByEngName(@RequestParam String engName) {
        List<ArtistResponse> artists = artistService.searchByEngName(engName);
        return ResponseEntity.ok(artists);
    }

    // 한국어 이름으로 검색
    @GetMapping("/search/artist/kor-name")
    public ResponseEntity<List<ArtistResponse>> searchByKorName(@RequestParam String korName) {
        List<ArtistResponse> artists = artistService.searchByKorName(korName);
        return ResponseEntity.ok(artists);
    }

    // 한글 이름, 영문 이름 포함하여 통합 검색
    @GetMapping("/search/artist")
    @Operation(summary = "화가 검색", description = "화가의 한글, 영문 이름을 기반으로 화가를 검색하는 API")
    public ResponseEntity<List<ArtistResponse>> searchArtist(@RequestParam String keyword) {
        List<ArtistResponse> artists = artistService.search(keyword);
        return ResponseEntity.ok(artists);
    }




}
