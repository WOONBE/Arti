package com.d106.arti.artwork.controller;

import com.d106.arti.artwork.dto.response.ArtistResponse;
import com.d106.arti.artwork.dto.response.NormalArtworkResponse;
import com.d106.arti.artwork.service.ArtistService;
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
class ArtistControllerTest {


    private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
    }


    @Test
    @DisplayName("통합 검색 - 'artist' 키워드로 검색했을 때 결과가 반환되는지 확인")
    void searchArtist() throws Exception {
        // Given
        String keyword = "artist";
        List<ArtistResponse> mockArtists = Arrays.asList(
            ArtistResponse.builder()
                .artist_id(1)
                .engName("Artist One")
                .korName("아티스트 원")
                .image("artist1.jpg")
                .summary("Summary One")
                .build(),
            ArtistResponse.builder()
                .artist_id(2)
                .engName("Artist Two")
                .korName("아티스트 투")
                .image("artist2.jpg")
                .summary("Summary Two")
                .build()
        );

        when(artistService.search(keyword)).thenReturn(mockArtists);

        // When & Then
        mockMvc.perform(get("/artists/search")
                .param("keyword", keyword)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("["
                + "{'artist_id':1,'engName':'Artist One','korName':'아티스트 원','image':'artist1.jpg','summary':'Summary One'},"
                + "{'artist_id':2,'engName':'Artist Two','korName':'아티스트 투','image':'artist2.jpg','summary':'Summary Two'}"
                + "]"));

        verify(artistService, times(1)).search(keyword);



    }


    @Test
    @DisplayName("단건 조회 - artistId 1번 아티스트가 정상적으로 조회되는지 확인")
    void findArtistById() throws Exception {
        // Given
        Integer artistId = 1;
        ArtistResponse mockArtist = ArtistResponse.builder()
            .artist_id(1)
            .engName("Artist One")
            .korName("아티스트 원")
            .image("artist1.jpg")
            .summary("Summary One")
            .build();

        when(artistService.findArtistById(artistId)).thenReturn(mockArtist);

        // When & Then
        mockMvc.perform(get("/artists/{artistId}", artistId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{"
                + "'artist_id':1,"
                + "'engName':'Artist One',"
                + "'korName':'아티스트 원',"
                + "'image':'artist1.jpg',"
                + "'summary':'Summary One'"
                + "}"));

        verify(artistService, times(1)).findArtistById(artistId);

    }
}