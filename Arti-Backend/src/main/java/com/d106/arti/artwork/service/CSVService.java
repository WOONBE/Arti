package com.d106.arti.artwork.service;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.repository.ArtistRepository;
import com.d106.arti.artwork.repository.ArtworkRepository;
import com.opencsv.CSVReader;

import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVService {

    private final ArtworkRepository artworkRepository;

    private final ArtistRepository artistRepository;

    public void readCSVAndSaveData(String filename) {
        List<NormalArtWork> artworks = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] nextLine;
            csvReader.readNext(); // 헤더 스킵
            while ((nextLine = csvReader.readNext()) != null) {
                try {
                    // CSV에서 가져온 artist 이름을 기반으로 Artist 엔티티 조회
                    String artistName = nextLine[1];
                    Artist artist = artistRepository.findByEngName(artistName);

                    // artist가 존재하지 않으면 에러 출력 및 continue
                    if (artist == null) {
//                        System.err.println("해당 이름의 아티스트를 찾을 수 없습니다: " + artistName);
                        continue; // 다음 레코드로 넘어감
                    }

                    // Artwork 엔티티 생성
                    NormalArtWork artwork = NormalArtWork.builder()
                        .filename(nextLine[0])
                        .artistName(artistName)
                        .genre(nextLine[2])
                        .description(nextLine[3])
                        .phash(nextLine[4])
                        .width(parseInteger(nextLine[5], "width"))
                        .height(parseInteger(nextLine[6], "height"))
                        .genreCount(parseInteger(nextLine[7], "genreCount"))
                        .subset(nextLine[8])
                        .artistKo(nextLine[9])
                        .title(nextLine[10])
                        .year(nextLine[11])
                        .build();

                    // 연관관계 설정
                    artwork.updateArtist(artist);

                    artworks.add(artwork);
                } catch (NumberFormatException e) {
                    System.err.println("잘못된 숫자 형식이 있습니다: " + e.getMessage());
                }
            }
            artworkRepository.saveAll(artworks);
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("알 수 없는 오류: " + e.getMessage());
        }
    }

    public void readArtistCSVAndSaveData(String filename) {
        List<Artist> artists = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] nextLine;
            csvReader.readNext(); // 첫 번째 줄(헤더) 건너뜀
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 4) {
                    System.err.println("잘못된 데이터 형식: " + String.join(",", nextLine));
                    continue;
                }
                String engName = nextLine.length > 0 ? nextLine[0] : "";
                String korName = nextLine.length > 1 ? nextLine[1] : "";
                String summary = nextLine.length > 2 ? nextLine[2] : "";
                String imageUrl = nextLine.length > 3 ? nextLine[3] : "";
                Artist artist = Artist.builder()
                    .engName(engName)
                    .korName(korName)
                    .summary(summary)
                    .image(imageUrl)
                    .build();
                artists.add(artist);
            }
            artistRepository.saveAll(artists);
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("알 수 없는 오류: " + e.getMessage());
        }
    }


    private int parseInteger(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            System.err.println("빈 문자열은 숫자로 변환할 수 없습니다: " + fieldName);
            return 0; // 기본값으로 0을 사용하거나, 필요에 따라 다른 값을 사용할 수 있습니다.
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("숫자 형식 오류: " + value + " (" + fieldName + ")");
            return 0; // 기본값으로 0을 사용하거나, 필요에 따라 다른 값을 사용할 수 있습니다.
        }
    }


}
