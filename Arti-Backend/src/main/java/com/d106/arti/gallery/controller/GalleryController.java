package com.d106.arti.gallery.controller;

import com.d106.arti.artwork.dto.response.ArtworkResponse;
import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.dto.request.GalleryRequest;
import com.d106.arti.gallery.dto.request.ThemeRequest;
import com.d106.arti.gallery.dto.response.GalleryResponse;
import com.d106.arti.gallery.dto.response.SubscribedGalleryResponse;
import com.d106.arti.gallery.dto.response.ThemeResponse;
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
    @PostMapping("/{galleryId}/themes")
    @Operation(summary = "테마 생성", description = "특정 미술관에 테마를 생성하는 API")
    public ResponseEntity<ThemeResponse> createTheme(@PathVariable Integer galleryId, @RequestBody ThemeRequest themeRequest) {
        themeRequest.setGalleryId(galleryId); // galleryId를 ThemeRequestDto에 설정
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

    // 4. 테마에서 미술품 삭제
    @DeleteMapping("/themes/{themeId}/artworks/{artworkId}")
    @Operation(summary = "테마의 미술품 삭제", description = "특정 테마의 미술품을 삭제하는 API")
    public ResponseEntity<Void> removeArtworkFromTheme(@PathVariable Integer themeId, @PathVariable Integer artworkId) {
        galleryService.removeArtworkFromTheme(themeId, artworkId);
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




//    // 7. 미술관 생성
//    @PostMapping
//    @Operation(summary = "미술관 생성", description = "새로운 미술관을 생성하는 API")
//    public ResponseEntity<GalleryResponse> createGallery(@RequestBody GalleryRequest galleryRequest) {
//        GalleryResponse galleryResponse = galleryService.createGallery(galleryRequest);
//        return ResponseEntity.ok(galleryResponse);
//    }
//// 7. 미술관 생성 (MultipartFile로 이미지 업로드)
//    @PostMapping
//    @Operation(summary = "미술관 생성", description = "새로운 미술관을 생성하는 API")
//    public ResponseEntity<GalleryResponse> createGallery(
//        @RequestPart("galleryRequest") GalleryRequest galleryRequest,
//        @RequestPart("image") MultipartFile image) {
//
//        // 이미지 파일을 GalleryRequest에 설정
//        galleryRequest.updateImage(image);
//
//        // 서비스 호출하여 갤러리 생성
//        GalleryResponse galleryResponse = galleryService.createGallery(galleryRequest);
//        return ResponseEntity.ok(galleryResponse);
//    }

    // 8. 미술관 상세 조회
    @GetMapping("/{galleryId}")
    @Operation(summary = "미술관 상세 조회", description = "특정 미술관의 상세 정보를 조회하는 API")
    public ResponseEntity<GalleryResponse> getGalleryById(@PathVariable Integer galleryId) {
        GalleryResponse galleryResponse = galleryService.getGalleryById(galleryId);
        return ResponseEntity.ok(galleryResponse);
    }



//    // 9. 미술관 정보 수정
//    @PutMapping("/{galleryId}")
//    @Operation(summary = "미술관 정보 수정", description = "특정 미술관의 정보를 수정하는 API")
//    public ResponseEntity<GalleryResponse> updateGallery(@PathVariable Integer galleryId, @RequestBody GalleryRequest galleryRequest) {
//        GalleryResponse updatedGallery = galleryService.updateGallery(galleryId, galleryRequest);
//        return ResponseEntity.ok(updatedGallery);
//    }
//    // 9. 미술관 정보 수정 (MultipartFile로 이미지 업로드)
//    @PutMapping("/{galleryId}")
//    @Operation(summary = "미술관 정보 수정", description = "특정 미술관의 정보를 수정하는 API")
//    public ResponseEntity<GalleryResponse> updateGallery(
//        @PathVariable Integer galleryId,
//        @RequestPart("galleryRequest") GalleryRequest galleryRequest,
//        @RequestPart(value = "image", required = false) MultipartFile image) {
//
//        // 이미지 파일이 있으면 설정
//        if (image != null) {
//            galleryRequest.updateImage(image);
//        }
//
//        // 서비스 호출하여 갤러리 정보 수정
//        GalleryResponse updatedGallery = galleryService.updateGallery(galleryId, galleryRequest);
//        return ResponseEntity.ok(updatedGallery);
//    }


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
        // 장르 레이블로 미술품 조회 서비스 호출
        List<ArtworkResponse> randomArtworks = galleryService.getRandomArtworksByGenre(genreLabel);
        // 조회된 미술품 목록을 반환
        return ResponseEntity.ok(randomArtworks);
    }

    @GetMapping("/subscriptions/{memberId}")
    @Operation(summary = "사용자가 구독한 미술관 목록 조회", description = "사용자가 구독한 미술관 정보를 모두 조회하는 API")
    public ResponseEntity<List<SubscribedGalleryResponse>> getSubscribedGalleries(@PathVariable Integer memberId) {

        // 서비스에서 구독한 갤러리 목록을 조회
        List<SubscribedGalleryResponse> subscribedGalleries = galleryService.getSubscribedGalleriesByMemberId(memberId);
        // 조회 결과를 반환
        return ResponseEntity.ok(subscribedGalleries);
    }




}