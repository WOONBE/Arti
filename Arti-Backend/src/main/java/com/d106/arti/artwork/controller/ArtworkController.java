package com.d106.arti.artwork.controller;



import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.service.ArtworkService;
import com.d106.arti.artwork.service.CSVService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/import-csv/{filename}")
    public String importCSV(@PathVariable String filename) {
        String filePath = "src/main/resources/" + filename;
        int limit = 100; // Limit the number of rows to 100
        csvService.readCSVAndSaveData(filePath, limit);
        return "Successfully imported 100 records from " + filename;
    }

    // 새로운 Artist CSV 파일 처리 엔드포인트
    @GetMapping("/import-artist-csv/{filename}")
    public String importArtistCSV(@PathVariable String filename) {
        String filePath = "src/main/resources/" + filename;
        int limit = 100; // 최대 100개의 데이터를 가져옴
        csvService.readArtistCSVAndSaveData(filePath, limit);
        return "성공적으로 " + filename + " 파일에서 100개의 아티스트 데이터를 가져왔습니다.";
    }

    //title과 description 기준으로 부분 검색
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<NormalArtWork>> searchArtworks(@PathVariable String keyword) {
        List<NormalArtWork> artworks = artworkService.searchArtworks(keyword);

        if (artworks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(artworks);
        }
    }


}
