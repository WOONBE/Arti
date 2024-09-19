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

//    public void readCSVAndSaveData(String filePath, int limit) {
//        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
//            String[] nextLine;
//            List<NormalArtWork> artworks = new ArrayList<>();
//
//            csvReader.readNext(); // 헤더 스킵
//            int count = 0;
//            while ((nextLine = csvReader.readNext()) != null && count < limit) {
//                NormalArtWork artwork = null;
//
//                try {
//                    artwork = NormalArtWork.builder()
//                        .filename(nextLine[0])
//                        .artist(nextLine[1])
//                        .genre(nextLine[2])
//                        .description(nextLine[3])
//                        .phash(nextLine[4])
//                        .width(parseInteger(nextLine[5], "width"))
//                        .height(parseInteger(nextLine[6], "height"))
//                        .genreCount(parseInteger(nextLine[7], "genreCount"))
//                        .subset(nextLine[8])
//                        .artistKo(nextLine[9])
//                        .title(nextLine[10])
//                        .year(nextLine[11]) // year 필드를 문자열로 저장
//                        .build();
//                } catch (NumberFormatException e) {
//                    System.err.println("잘못된 숫자 형식이 있습니다: " + e.getMessage());
//                    continue; // 잘못된 데이터가 있으면 해당 레코드를 건너뜁니다.
//                }
//
//                artworks.add(artwork);
//                count++;
//            }
//            artworkRepository.saveAll(artworks); // 모든 artworks를 DB에 저장
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void readArtistCSVAndSaveData(String filePath, int limit) {
//        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
//            String[] nextLine;
//            List<Artist> artists = new ArrayList<>();
//
//            csvReader.readNext(); // 첫 번째 줄(헤더) 건너뜀
//            int count = 0;
//            while ((nextLine = csvReader.readNext()) != null && count < limit) {
//                if (nextLine.length < 4) {
//                    System.out.println("잘못된 데이터 형식: " + String.join(",", nextLine));
//                    continue;
//                }
//
//                String engName = nextLine.length > 0 ? nextLine[0] : "";
//                String korName = nextLine.length > 1 ? nextLine[1] : "";
//                String summary = nextLine.length > 2 ? nextLine[2] : "";
//                String imageUrl = nextLine.length > 3 ? nextLine[3] : "";
//
//                Artist artist = Artist.builder()
//                    .eng_name(engName)
//                    .kor_name(korName)
//                    .summary(summary)
//                    .image(imageUrl)
//                    .build();
//
//                artists.add(artist);
//                count++;
//            }
//
//            artistRepository.saveAll(artists); // 아티스트 데이터 DB에 저장
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
public void readCSVAndSaveData(String filename, int limit) {
    List<NormalArtWork> artworks = new ArrayList<>();
    Resource resource = new ClassPathResource(filename);

    try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
        String[] nextLine;
        csvReader.readNext(); // 헤더 스킵
        int count = 0;
        while ((nextLine = csvReader.readNext()) != null && count < limit) {
            try {
                NormalArtWork artwork = NormalArtWork.builder()
                    .filename(nextLine[0])
                    .artist(nextLine[1])
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
                artworks.add(artwork);
                count++;
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

    public void readArtistCSVAndSaveData(String filename, int limit) {
        List<Artist> artists = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] nextLine;
            csvReader.readNext(); // 첫 번째 줄(헤더) 건너뜀
            int count = 0;
            while ((nextLine = csvReader.readNext()) != null && count < limit) {
                if (nextLine.length < 4) {
                    System.err.println("잘못된 데이터 형식: " + String.join(",", nextLine));
                    continue;
                }
                String engName = nextLine.length > 0 ? nextLine[0] : "";
                String korName = nextLine.length > 1 ? nextLine[1] : "";
                String summary = nextLine.length > 2 ? nextLine[2] : "";
                String imageUrl = nextLine.length > 3 ? nextLine[3] : "";
                Artist artist = Artist.builder()
                    .eng_name(engName)
                    .kor_name(korName)
                    .summary(summary)
                    .image(imageUrl)
                    .build();
                artists.add(artist);
                count++;
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
