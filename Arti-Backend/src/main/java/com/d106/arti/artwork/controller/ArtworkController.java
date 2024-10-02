package com.d106.arti.artwork.controller;



import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.dto.response.ArtworkResponse;
import com.d106.arti.artwork.dto.response.NormalArtworkResponse;
import com.d106.arti.artwork.service.ArtworkService;
import com.d106.arti.artwork.service.CSVService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artworks")
public class ArtworkController {

    private final CSVService csvService;

    private final ArtworkService artworkService;

    private static final Logger logger = LoggerFactory.getLogger(CSVService.class);

    @GetMapping("/import-csv/{filename}")
    public ResponseEntity<String> importCSV(@PathVariable String filename) {
        Resource resource = new ClassPathResource(filename);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("파일이 존재하지 않습니다: " + filename);
        }

        try {
            csvService.readCSVAndSaveData(filename); // Remove limit
            return new ResponseEntity<>("Successfully imported records from " + filename, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error importing CSV: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/import-artist-csv/{filename}")
    public ResponseEntity<String> importArtistCSV(@PathVariable String filename) {
        Resource resource = new ClassPathResource(filename);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("파일이 존재하지 않습니다: " + filename);
        }

        try {
            csvService.readArtistCSVAndSaveData(filename); // Remove limit
            return new ResponseEntity<>("Successfully imported records from " + filename, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error importing artist CSV: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/search")
//    @Operation(summary = "미술품 검색", description = "미술품의 title, description으로 검색하는 API")
//    public ResponseEntity<List<NormalArtworkResponse>> searchArtworks(@RequestParam String keyword) {
//        List<NormalArtworkResponse> artworks = artworkService.searchArtworks(keyword);
//        return ResponseEntity.ok(artworks);
//    }

    @GetMapping("/search")
    @Operation(summary = "미술품 검색", description = "키워드를 통해 미술품을 검색하며, 결과를 페이징 처리하여 30개씩 반환하는 API")
    public ResponseEntity<Page<NormalArtworkResponse>> searchArtworks(
        @RequestParam String keyword, // 검색 키워드
        @RequestParam(defaultValue = "1") int page  // 기본적으로 1 페이지부터 시작
    ) {
        Page<NormalArtworkResponse> artworks = artworkService.searchArtworks(keyword, page);
        return ResponseEntity.ok(artworks);
    }

    // 단건 조회 엔드포인트
    @GetMapping("/{artworkId}")
    @Operation(summary = "미술품 단건 조회", description = "미술품 ID로 단건을 조회하는 API")
    public ResponseEntity<NormalArtworkResponse> getArtworkById(@PathVariable Integer artworkId) {
        NormalArtworkResponse artworkResponse = artworkService.getArtworkById(artworkId);
        return ResponseEntity.ok(artworkResponse);
    }

    // 랜덤한 50개의 미술품 조회
    @GetMapping("/random")
    @Operation(summary = "미술품 랜덤 조회", description = "랜덤한 50개의 미술품 조회하는 API")
    public ResponseEntity<List<ArtworkResponse>> getRandomArtworks() {
        List<ArtworkResponse> randomArtworks = artworkService.getRandomArtworks();
        return ResponseEntity.ok(randomArtworks);
    }
}
