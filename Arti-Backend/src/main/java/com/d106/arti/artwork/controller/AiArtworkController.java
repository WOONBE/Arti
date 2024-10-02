package com.d106.arti.artwork.controller;

import com.d106.arti.artwork.dto.request.AiArtworkRequest;
import com.d106.arti.artwork.dto.response.AiArtworkResponse;
import com.d106.arti.artwork.service.AiArtworkService;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.global.exception.ExceptionCode;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("artworks/ai")
@RequiredArgsConstructor
public class AiArtworkController {

    private final AiArtworkService aiArtworkService;

    // AI 미술품 생성
    @PostMapping
    @Operation(summary = "AI 미술품 생성", description = "AI 미술품 정보를 받아 미술품을 생성하는 API")
    public ResponseEntity<AiArtworkResponse> createAiArtwork(@RequestBody AiArtworkRequest request) {

            AiArtworkResponse response = aiArtworkService.createAiArtwork(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // AI 미술품 삭제
    @DeleteMapping("/{aiArtworkId}")
    @Operation(summary = "AI 미술품 삭제", description = "AI 미술품이 있으면 삭제하는 API")
    public ResponseEntity<Void> deleteAiArtwork(@PathVariable Integer aiArtworkId) {
            aiArtworkService.deleteAiArtwork(aiArtworkId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 특정 멤버의 AI 미술품 목록 조회
    @GetMapping("/member/{memberId}")
    @Operation(summary = "특정 멤버의 AI 미술품 목록 조회", description = "특정 멤버의 AI 미술품 목록 조회하는 API")
    public ResponseEntity<List<AiArtworkResponse>> getAiArtworksByMemberId(@PathVariable Integer memberId) {
        List<AiArtworkResponse> aiArtworks = aiArtworkService.getAiArtworksByMemberId(memberId);
        return ResponseEntity.ok(aiArtworks);
    }
}
