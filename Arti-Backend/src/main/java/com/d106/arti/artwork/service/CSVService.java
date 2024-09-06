package com.d106.arti.artwork.service;

import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.repository.ArtworkRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVService {

    private final ArtworkRepository artworkRepository;

    public void readCSVAndSaveData(String filePath, int limit) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            List<NormalArtWork> artworks = new ArrayList<>();

            csvReader.readNext(); // 헤더 스킵
            int count = 0;
            while ((nextLine = csvReader.readNext()) != null && count < limit) {
                NormalArtWork artwork = null;

                try {
                    artwork = NormalArtWork.builder()
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
                        .year(nextLine[11]) // year 필드를 문자열로 저장
                        .build();
                } catch (NumberFormatException e) {
                    System.err.println("잘못된 숫자 형식이 있습니다: " + e.getMessage());
                    continue; // 잘못된 데이터가 있으면 해당 레코드를 건너뜁니다.
                }

                artworks.add(artwork);
                count++;
            }
            artworkRepository.saveAll(artworks); // 모든 artworks를 DB에 저장
        } catch (Exception e) {
            e.printStackTrace();
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
