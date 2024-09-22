package com.d106.arti.gallery.controller;

import com.d106.arti.gallery.dto.request.GalleryRequest;
import com.d106.arti.gallery.dto.response.GalleryResponse;
import com.d106.arti.gallery.service.GalleryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/galleries")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    // 특정 회원의 갤러리 조회 (신규)
    @GetMapping("/{memberId}")
    @Operation(summary = "특정 회원의 갤러리 조회", description = "특정 회원이 소유한 갤러리를 조회하는 API")
    public ResponseEntity<GalleryResponse> getGalleryByMember(@PathVariable Integer memberId) {
        GalleryResponse gallery = galleryService.getGalleryByMember(memberId);
        return ResponseEntity.ok(gallery);
    }

    // 갤러리 정보 수정 (신규)
    @PutMapping("/{galleryId}")
    @Operation(summary = "갤러리 정보 수정", description = "갤러리의 타이틀, 설명, 이미지를 수정하는 API")
    public ResponseEntity<GalleryResponse> updateGallery(
            @PathVariable Integer galleryId,
            @RequestBody GalleryRequest request
    ) {
        GalleryResponse updatedGallery = galleryService.updateGallery(galleryId, request);
        return ResponseEntity.ok(updatedGallery);
    }
}
