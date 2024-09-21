package com.d106.arti.gallery.controller;

import com.d106.arti.gallery.dto.request.ThemeRequest;
import com.d106.arti.gallery.dto.response.ThemeResponse;
import com.d106.arti.gallery.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/galleries/{galleryId}/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    // 테마 전체 조회
    @GetMapping
    @Operation(summary = "갤러리의 테마 전체 조회", description = "특정 갤러리의 테마들을 조회하는 API")
    public ResponseEntity<List<ThemeResponse>> getThemesByGalleryId(@PathVariable Integer galleryId) {
        List<ThemeResponse> themes = themeService.getThemesByGalleryId(galleryId);
        return ResponseEntity.ok(themes);
    }

    // 테마 생성
    @PostMapping
    @Operation(summary = "테마 생성", description = "갤러리에 새로운 테마를 생성하는 API")
    public ResponseEntity<ThemeResponse> createTheme(@PathVariable Integer galleryId, @RequestBody ThemeRequest request) {
        ThemeResponse theme = themeService.createTheme(galleryId, request);
        return new ResponseEntity<>(theme, HttpStatus.CREATED);
    }

    // 테마 수정
    @PutMapping("/{themeId}")
    @Operation(summary = "테마 수정", description = "특정 테마의 제목을 수정하는 API")
    public ResponseEntity<ThemeResponse> updateTheme(@PathVariable Integer galleryId, @PathVariable Integer themeId, @RequestBody ThemeRequest request) {
        ThemeResponse theme = themeService.updateTheme(themeId, request);
        return ResponseEntity.ok(theme);
    }

    // 테마 삭제
    @DeleteMapping("/{themeId}")
    @Operation(summary = "테마 삭제", description = "특정 테마를 삭제하는 API")
    public ResponseEntity<Void> deleteTheme(@PathVariable Integer galleryId, @PathVariable Integer themeId) {
        themeService.deleteTheme(themeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
//    // 테마에 미술품 추가
//    @PostMapping("/{themeId}/artworks")
//    @Operation(summary = "테마에 미술품 추가", description = "특정 테마에 미술품을 추가하는 API")
//    public ResponseEntity<ArtworkResponse> addArtworkToTheme(@PathVariable Integer galleryId, @PathVariable Integer themeId, @RequestBody ArtworkRequest request) {
//        ArtworkResponse artwork = themeService.addArtworkToTheme(themeId, request);
//        return new ResponseEntity<>(artwork, HttpStatus.CREATED);
//    }
//
//    // 테마에서 미술품 삭제
//    @DeleteMapping("/{themeId}/artworks/{artworkId}")
//    @Operation(summary = "테마에서 미술품 삭제", description = "특정 테마에서 미술품을 삭제하는 API")
//    public ResponseEntity<Void> removeArtworkFromTheme(@PathVariable Integer galleryId, @PathVariable Integer themeId, @PathVariable Integer artworkId) {
//        themeService.removeArtworkFromTheme(themeId, artworkId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
