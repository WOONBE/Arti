package com.d106.arti.artwork.controller;

import com.d106.arti.artwork.dto.request.AiArtworkRequest;
import com.d106.arti.artwork.dto.response.AiArtworkResponse;
import com.d106.arti.artwork.service.AiArtworkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AiArtworkControllerTest {

    @Mock
    private AiArtworkService aiArtworkService;

    @InjectMocks
    private AiArtworkController aiArtworkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAiArtwork() {
        // given
        AiArtworkRequest request = AiArtworkRequest.builder()
            .aiArtworkTitle("병나리자")
            .artworkImage("mona.png")
            .genre("추상")
            .memberId(1)
            .themeId(2)
            .originalImageId(3)
            .build();

        AiArtworkResponse response = AiArtworkResponse.builder()
            .id(1)
            .aiArtworkTitle("병나리자")
            .artworkImage("mona.png")
            .genre("추상")
            .memberId(1)
            .themeId(2)
            .originalImageId(3)
            .build();

        // when
        when(aiArtworkService.createAiArtwork(any(AiArtworkRequest.class))).thenReturn(response);

        // then
        ResponseEntity<AiArtworkResponse> result = aiArtworkController.createAiArtwork(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("병나리자", result.getBody().getAiArtworkTitle());

    }

    @Test
    void deleteAiArtwork() {
        // given
        Integer aiArtworkId = 1;

        // 서비스의 delete 메서드를 mock 처리하여 실제 삭제가 이루어지지 않도록 함
        doNothing().when(aiArtworkService).deleteAiArtwork(aiArtworkId);

        // when
        ResponseEntity<Void> result = aiArtworkController.deleteAiArtwork(aiArtworkId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // 서비스 메서드가 한 번 호출되었는지 검증
        verify(aiArtworkService, times(1)).deleteAiArtwork(aiArtworkId);
    }

    @Test
    void getAiArtworksByMemberId() {
        // given
        List<AiArtworkResponse> responseList = Arrays.asList(
            AiArtworkResponse.builder()
                .id(1)
                .aiArtworkTitle("AI 미술작품1")
                .artworkImage("image1.png")
                .genre("추상")
                .memberId(1)
                .themeId(2)
                .originalImageId(3)
                .build(),
            AiArtworkResponse.builder()
                .id(2)
                .aiArtworkTitle("AI 미술작품2")
                .artworkImage("image2.png")
                .genre("사실주의")
                .memberId(1)
                .themeId(2)
                .originalImageId(4)
                .build()
        );

        // when
        when(aiArtworkService.getAiArtworksByMemberId(anyInt())).thenReturn(responseList);

        // then
        ResponseEntity<List<AiArtworkResponse>> result = aiArtworkController.getAiArtworksByMemberId(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

}