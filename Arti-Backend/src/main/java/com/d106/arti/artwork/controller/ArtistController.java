package com.d106.arti.artwork.controller;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.dto.response.ArtistResponse;
import com.d106.arti.artwork.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

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
    @GetMapping("/search")
    @Operation(summary = "화가 검색", description = "화가의 한글, 영문 이름을 기반으로 화가를 검색하는 API")
    public ResponseEntity<List<ArtistResponse>> searchArtist(@RequestParam String keyword) {
        List<ArtistResponse> artists = artistService.search(keyword);
        return ResponseEntity.ok(artists);
    }

    // 단건 조회
    @GetMapping("/{artistId}")
    @Operation(summary = "화가 단건 조회", description = "ID를 기반으로 화가 정보를 단건으로 상세 조회하는 API")
    public ResponseEntity<ArtistResponse> findArtistById(@PathVariable Integer artistId) {
        ArtistResponse artist = artistService.findArtistById(artistId);
        return ResponseEntity.ok(artist);
    }

    // 랜덤한 50명의 화가 조회
    @GetMapping("/random")
    @Operation(summary = "화가 랜덤 조회", description = "화가를 최대 50명까지 랜덤하게 조회하는 API")
    public ResponseEntity<List<ArtistResponse>> getRandomArtists() {
        List<ArtistResponse> randomArtists = artistService.getRandomArtists();
        return ResponseEntity.ok(randomArtists);  
    }

    @GetMapping("/by-genre")
    @Operation(summary = "장르별 대표 화가 조회", description = "장르별 대표 화가를 3명까지 조회하는 API")
    public ResponseEntity<List<ArtistResponse>> getArtistsByGenre(@RequestParam String genre) {
        List<ArtistResponse> artists = artistService.getArtistsByGenre(genre);
        return ResponseEntity.ok(artists);
    }






}
