package com.d106.arti.artwork.controller;



import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.dto.response.NormalArtworkResponse;
import com.d106.arti.artwork.service.ArtworkService;
import com.d106.arti.artwork.service.CSVService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artworks")
public class ArtworkController {

    private final CSVService csvService;

    private final ArtworkService artworkService;

    private static final Logger logger = LoggerFactory.getLogger(CSVService.class);

//    @GetMapping("/import-csv/{filename}")
//    public ResponseEntity<String> importCSV(@PathVariable String filename) {
//        Resource resource = new ClassPathResource(filename);
//
//        if (!resource.exists()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body("파일이 존재하지 않습니다: " + filename);
//        }
//
//        int limit = 100; // Limit the number of rows to 100
//        try {
//            csvService.readCSVAndSaveData(filename, limit);
//            return new ResponseEntity<>("Successfully imported 100 records from " + filename, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error importing CSV: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/import-artist-csv/{filename}")
//    public ResponseEntity<String> importArtistCSV(@PathVariable String filename) {
//        Resource resource = new ClassPathResource(filename);
//
//        if (!resource.exists()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body("파일이 존재하지 않습니다: " + filename);
//        }
//
//        int limit = 100; // Limit the number of rows to 100
//        try {
//            csvService.readArtistCSVAndSaveData(filename, limit);
//            return new ResponseEntity<>("Successfully imported 100 records from " + filename, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error importing artist CSV: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
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

    @GetMapping("/search")
    @Operation(summary = "미술품 검색", description = "미술품의 title, description으로 검색하는 API")
    public ResponseEntity<List<NormalArtworkResponse>> searchArtworks(@RequestParam String keyword) {
        List<NormalArtworkResponse> artworks = artworkService.searchArtworks(keyword);
        return ResponseEntity.ok(artworks);
    }

    // 단건 조회 엔드포인트
    @GetMapping("/{artworkId}")
    @Operation(summary = "미술품 단건 조회", description = "미술품 ID로 단건을 조회하는 API")
    public ResponseEntity<NormalArtworkResponse> getArtworkById(@PathVariable Integer artworkId) {
        NormalArtworkResponse artworkResponse = artworkService.getArtworkById(artworkId);
        return ResponseEntity.ok(artworkResponse);
    }


}