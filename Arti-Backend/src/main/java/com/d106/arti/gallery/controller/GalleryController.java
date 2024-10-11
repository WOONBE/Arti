package com.d106.arti.gallery.controller;

import com.d106.arti.artwork.dto.response.ArtworkResponse;
import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.dto.request.GalleryRequest;
import com.d106.arti.gallery.dto.request.ThemeRequest;
import com.d106.arti.gallery.dto.response.GalleryResponse;
import com.d106.arti.gallery.dto.response.SubscribedGalleryResponse;
import com.d106.arti.gallery.dto.response.ThemeResponse;
import com.d106.arti.gallery.dto.response.ThemeWithArtworksResponse;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.gallery.service.GalleryService;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/galleries")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;
    private final MemberRepository memberRepository;

    // 1. 특정 미술관의 테마 전체 조회
    @GetMapping("/{galleryId}/themes")
    @Operation(summary = "테마 전체 조회", description = "특정 미술관 ID를 기반으로 테마를 전체 조회하는 API")
    public ResponseEntity<List<ThemeResponse>> getAllThemesByGalleryId(@PathVariable Integer galleryId) {
        List<ThemeResponse> themes = galleryService.getAllThemesByGalleryId(galleryId);
        return ResponseEntity.ok(themes);
    }

    // 2. 특정 미술관에 테마 생성
    @PostMapping("/themes")
    @Operation(summary = "테마 생성", description = "특정 미술관에 테마를 생성하는 API")
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest themeRequest) {
        ThemeResponse themeResponse = galleryService.createTheme(themeRequest);
        return ResponseEntity.ok(themeResponse);
    }

    // 3. 테마에 미술품 추가
    @PostMapping("/themes/{themeId}/artworks/{artworkId}")
    @Operation(summary = "테마에 미술품 추가", description = "특정 테마에 미술품을 추가하는 API")
    public ResponseEntity<Void> addArtworkToTheme(@PathVariable Integer themeId, @PathVariable Integer artworkId, @RequestParam String description) {
        galleryService.addArtworkToTheme(themeId, artworkId, description);
        return ResponseEntity.ok().build();
    }

    // 3.5 테마에 AI미술품 추가
    @PostMapping("/themes/{themeId}/aiartworks/{artworkId}")
    @Operation(summary = "테마에 AI미술품 추가", description = "특정 테마에 AI미술품을 추가하는 API")
    public ResponseEntity<Void> addAiArtworkToTheme(@PathVariable Integer themeId, @PathVariable Integer artworkId, @RequestParam String description) {
        galleryService.addAiArtworkToTheme(themeId, artworkId, description);
        return ResponseEntity.ok().build();
    }

    // 4. 테마에서 미술품 삭제
    @DeleteMapping("/themes/{themeId}/artworks/{artworkId}")
    @Operation(summary = "테마의 미술품 삭제", description = "특정 테마의 미술품을 삭제하는 API")
    public ResponseEntity<Void> removeArtworkFromTheme(@PathVariable Integer themeId, @PathVariable Integer artworkId) {
        galleryService.removeArtworkFromTheme(themeId, artworkId);
        return ResponseEntity.ok().build();
    }

    // 4.5 테마에서 AI미술품 삭제
    @DeleteMapping("/themes/{themeId}/aiartworks/{artworkId}")
    @Operation(summary = "테마의 Ai미술품 삭제", description = "특정 테마의 Ai미술품을 삭제하는 API")
    public ResponseEntity<Void> removeAiArtworkFromTheme(@PathVariable Integer themeId, @PathVariable Integer artworkId) {
        galleryService.removeAiArtworkFromTheme(themeId, artworkId);
        return ResponseEntity.ok().build();
    }

    // 5. 테마 수정
    @PutMapping("/themes/{themeId}")
    @Operation(summary = "테마 수정", description = "특정 테마의 정보를 수정하는 API")
    public ResponseEntity<ThemeResponse> updateTheme(@PathVariable Integer themeId, @RequestBody ThemeRequest themeRequest) {
        ThemeResponse updatedTheme = galleryService.updateTheme(themeId, themeRequest);
        return ResponseEntity.ok(updatedTheme);
    }

    // 6. 특정 미술관의 특정 테마 삭제
    @DeleteMapping("/{galleryId}/themes/{themeId}")
    @Operation(summary = "테마 삭제", description = "특정 테마를 삭제하는 API")
    public ResponseEntity<Void> deleteTheme(@PathVariable Integer galleryId, @PathVariable Integer themeId) {
        galleryService.deleteTheme(galleryId, themeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/random")
    @Operation(summary = "랜덤 미술관 조회", description = "랜덤하게 50개의 미술관을 조회하는 API")
    public ResponseEntity<List<GalleryResponse>> getRandomGalleries() {
        List<GalleryResponse> randomGalleries = galleryService.getRandomGalleries();
        return ResponseEntity.ok(randomGalleries);
    }


    // 8. 미술관 상세 조회
    @GetMapping("/{galleryId}")
    @Operation(summary = "미술관 상세 조회", description = "특정 미술관의 상세 정보를 조회하는 API")
    public ResponseEntity<GalleryResponse> getGalleryById(@PathVariable Integer galleryId) {
        GalleryResponse galleryResponse = galleryService.getGalleryById(galleryId);
        return ResponseEntity.ok(galleryResponse);
    }


    // 1. 미술관 생성 (MultipartFile 사용)
    @PostMapping
    @Operation(summary = "미술관 생성", description = "새로운 미술관을 생성하는 API")
    public ResponseEntity<GalleryResponse> createGallery(
        @RequestPart("galleryRequest") GalleryRequest galleryRequest,
        @RequestPart("image") MultipartFile image) {
        GalleryResponse galleryResponse = galleryService.createGallery(galleryRequest, image);
        return ResponseEntity.ok(galleryResponse);
    }

    // 2. 미술관 정보 수정 (MultipartFile 사용)
    @PutMapping("/{galleryId}")
    @Operation(summary = "미술관 정보 수정", description = "특정 미술관의 정보를 수정하는 API")
    public ResponseEntity<GalleryResponse> updateGallery(
        @PathVariable Integer galleryId,
        @RequestPart("galleryRequest") GalleryRequest galleryRequest,
        @RequestPart(value = "image", required = false) MultipartFile image) {
        GalleryResponse updatedGallery = galleryService.updateGallery(galleryId, galleryRequest, image);
        return ResponseEntity.ok(updatedGallery);
    }


    // 특정 테마에 속한 모든 미술품 조회
    @GetMapping("/{themeId}/artworks")
    @Operation(summary = "테마의 미술품 조회", description = "특정 테마에 속한 모든 미술품을 조회하는 API")
    public ResponseEntity<List<ArtworkResponse>> getArtworksByTheme(@PathVariable Integer themeId) {
        List<ArtworkResponse> artworks = galleryService.getArtworksByThemeId(themeId);
        return ResponseEntity.ok(artworks);
    }

    // 특정 장르에 대한 랜덤 미술품 50개 조회
    @GetMapping("/artworks/random")
    @Operation(summary = "특정 장르의 미술품 랜덤 조회", description = "특정 장르에 속한 미술품을 랜덤하게 50개 조회하는 API, 검색시 _중간에 넣고 검색")
    public ResponseEntity<List<ArtworkResponse>> getRandomArtworksByGenre(@RequestParam String genreLabel) {

        List<ArtworkResponse> randomArtworks = galleryService.getRandomArtworksByGenre(genreLabel);

        return ResponseEntity.ok(randomArtworks);
    }

    @GetMapping("/subscriptions/{memberId}")
    @Operation(summary = "사용자가 구독한 미술관 목록 조회", description = "사용자가 구독한 미술관 정보를 모두 조회하는 API")
    public ResponseEntity<List<SubscribedGalleryResponse>> getSubscribedGalleries(@PathVariable Integer memberId) {


        List<SubscribedGalleryResponse> subscribedGalleries = galleryService.getSubscribedGalleriesByMemberId(memberId);

        return ResponseEntity.ok(subscribedGalleries);
    }

    @GetMapping("/search")
    @Operation(summary = "미술관 검색", description = "미술관 이름을 기준으로 검색하는 API")
    public ResponseEntity<List<GalleryResponse>> searchGalleryByName(@RequestParam String keyword) {
        List<GalleryResponse> galleries = galleryService.searchGalleryByName(keyword);
        return ResponseEntity.ok(galleries);
    }



    // 특정 미술관의 테마와 테마에 속한 모든 미술품 조회
    @GetMapping("/{galleryId}/themes-with-artworks")
    @Operation(summary = "미술관 내부 테마 및 미술품 전체 조회", description = "미술관 내부 테마 및 미술품 전체 조회하는 API")
    public ResponseEntity<List<ThemeWithArtworksResponse>> getAllThemesWithArtworksByGalleryId(
        @PathVariable Integer galleryId) {

        List<ThemeWithArtworksResponse> themesWithArtworks = galleryService.getAllThemesWithArtworksByGalleryId(galleryId);
        return ResponseEntity.ok(themesWithArtworks);
    }

}
