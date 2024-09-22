//package com.d106.arti.gallery.controller;
//
//import com.d106.arti.gallery.dto.request.GalleryRequest;
//import com.d106.arti.gallery.dto.response.GalleryResponse;
//import com.d106.arti.gallery.service.GalleryService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/galleries")
//@RequiredArgsConstructor
//public class GalleryController {
//
//    private final GalleryService galleryService;
//
//    @PostMapping
//    @Operation(summary = "갤러리 생성", description = "회원가입 시 갤러리 생성")
//    public ResponseEntity<GalleryResponse> createGallery(@RequestBody GalleryRequest request) {
//        GalleryResponse gallery = galleryService.createGallery(request);
//        return new ResponseEntity<>(gallery, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{galleryId}")
//    @Operation(summary = "갤러리 정보 수정", description = "갤러리 정보를 수정하는 API")
//    public ResponseEntity<GalleryResponse> updateGallery(@PathVariable Integer galleryId, @RequestBody GalleryRequest request) {
//        GalleryResponse gallery = galleryService.updateGallery(galleryId, request);
//        return ResponseEntity.ok(gallery);
//    }
//
//    @GetMapping("/{galleryId}")
//    @Operation(summary = "갤러리 조회", description = "특정 갤러리 정보를 조회하는 API")
//    public ResponseEntity<GalleryResponse> getGallery(@PathVariable Integer galleryId) {
//        GalleryResponse gallery = galleryService.getGallery(galleryId);
//        return ResponseEntity.ok(gallery);
//    }
//
//    @DeleteMapping("/{galleryId}")
//    @Operation(summary = "갤러리 삭제", description = "회원탈퇴 시 갤러리를 삭제하는 API")
//    public ResponseEntity<Void> deleteGallery(@PathVariable Integer galleryId) {
//        galleryService.deleteGallery(galleryId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
