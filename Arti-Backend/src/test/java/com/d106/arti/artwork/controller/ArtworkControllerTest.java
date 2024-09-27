package com.d106.arti.artwork.controller;

import com.d106.arti.artwork.dto.response.NormalArtworkResponse;
import com.d106.arti.artwork.service.ArtworkService;
import com.d106.arti.artwork.service.CSVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ArtworkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CSVService csvService;

    @Mock
    private ArtworkService artworkService;

    @InjectMocks
    private ArtworkController artworkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(artworkController).build();
    }

//    @Test
//    @DisplayName("검색 로직 - modern으로 검색했을때 1개의 항목만 검색되는지 확인")
//    void searchArtworks_ShouldReturnThreeArtworks() throws Exception {
//        // Given
//        String keyword = "modern";
//
//        List<NormalArtworkResponse> mockArtworks = Arrays.asList(
//            NormalArtworkResponse.builder()
//                .artwork_id(1)
//                .filename("art1.jpg")
//                .artist("Artist1")
//                .genre("Modern")
//                .description("Description1")
//                .phash("phash1")
//                .width(500)
//                .height(700)
//                .genreCount(1)
//                .subset("subset1")
//                .artistKo("ArtistKo1")
//                .title("Title1")
//                .year("2020")
//                .build(),
//            NormalArtworkResponse.builder()
//                .artwork_id(2)
//                .filename("art2.jpg")
//                .artist("Artist2")
//                .genre("Abstract")
//                .description("Description2")
//                .phash("phash2")
//                .width(600)
//                .height(800)
//                .genreCount(2)
//                .subset("subset2")
//                .artistKo("ArtistKo2")
//                .title("Title2")
//                .year("2021")
//                .build(),
//            NormalArtworkResponse.builder()
//                .artwork_id(3)
//                .filename("art3.jpg")
//                .artist("Artist3")
//                .genre("Contemporary")
//                .description("Description3")
//                .phash("phash3")
//                .width(700)
//                .height(900)
//                .genreCount(3)
//                .subset("subset3")
//                .artistKo("ArtistKo3")
//                .title("Title3")
//                .year("2022")
//                .build()
//        );
//
//        when(artworkService.searchArtworks(keyword)).thenReturn(mockArtworks);
//
//        // When & Then
//        mockMvc.perform(get("/artworks/search")
//                .param("keyword", keyword)
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(content().json("["
//                + "{'artwork_id':1,'filename':'art1.jpg','artist':'Artist1','genre':'Modern','description':'Description1','phash':'phash1','width':500,'height':700,'genreCount':1,'subset':'subset1','artistKo':'ArtistKo1','title':'Title1','year':'2020'},"
//                + "{'artwork_id':2,'filename':'art2.jpg','artist':'Artist2','genre':'Abstract','description':'Description2','phash':'phash2','width':600,'height':800,'genreCount':2,'subset':'subset2','artistKo':'ArtistKo2','title':'Title2','year':'2021'},"
//                + "{'artwork_id':3,'filename':'art3.jpg','artist':'Artist3','genre':'Contemporary','description':'Description3','phash':'phash3','width':700,'height':900,'genreCount':3,'subset':'subset3','artistKo':'ArtistKo3','title':'Title3','year':'2022'}"
//                + "]"));
//
//        verify(artworkService, times(1)).searchArtworks(keyword);
//    }

    @Test
    @DisplayName("단건 조회 - artworkId 1번 작품이 정상적으로 조회되는지 확인")
    void getArtworkById_ShouldReturnArtwork() throws Exception {
        // Given
        Integer artworkId = 1;

        NormalArtworkResponse mockArtwork = NormalArtworkResponse.builder()
            .artwork_id(1)
            .filename("art1.jpg")
            .artist("Artist1")
            .genre("Modern")
            .description("Description1")
            .phash("phash1")
            .width(500)
            .height(700)
            .genreCount(1)
            .subset("subset1")
            .artistKo("ArtistKo1")
            .title("Title1")
            .year("2020")
            .build();

        when(artworkService.getArtworkById(artworkId)).thenReturn(mockArtwork);

        // When & Then
        mockMvc.perform(get("/artworks/{artworkId}", artworkId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{"
                + "'artwork_id':1,"
                + "'filename':'art1.jpg',"
                + "'artist':'Artist1',"
                + "'genre':'Modern',"
                + "'description':'Description1',"
                + "'phash':'phash1',"
                + "'width':500,"
                + "'height':700,"
                + "'genreCount':1,"
                + "'subset':'subset1',"
                + "'artistKo':'ArtistKo1',"
                + "'title':'Title1',"
                + "'year':'2020'"
                + "}"));

        verify(artworkService, times(1)).getArtworkById(artworkId);
    }


}
