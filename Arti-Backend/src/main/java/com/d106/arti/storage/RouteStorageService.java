package com.d106.arti.storage;

import com.d106.arti.elasticsearch.entity.ViewCount;
import com.d106.arti.elasticsearch.repository.ViewCountRepository;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class RouteStorageService {

    private final S3StorageService s3StorageService;
    @Value("${elasticsearch.query}")
    private String query;
    @Value("${elasticsearch.url}")
    private String url;
    @Value("${server.image.base-url}")
    private String baseUrl;

    private final ViewCountRepository viewCountRepository;

    //    @Scheduled(cron = "0 0 0 * * *")
    public String routeStorage() {
        String credentials = "elastic:changeme";
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return WebClient.builder().build()
            .post()
            .uri(url)
            .header("content-type", "application/json")
            .header("Authorization", "Basic " + encodedCredentials)
            .bodyValue(query)
            .retrieve()
            .bodyToMono(ElasticResponse.class)
            .publishOn(Schedulers.boundedElastic())
            .<String>handle((response, sink) -> {
                List<ViewCount> viewCounts = response.getAggregations().getRequest_url_count()
                    .getBuckets().stream()
                    .map(bucket -> ViewCount.builder()
                        .requestURI(bucket.getKey())
                        .viewCount(bucket.getDoc_count())
                        .build()
                    )
                    .toList();
                // Bucket 데이터를 DB에 저장

                // 기존 이미지 S3에서 삭제
                //
                List<ViewCount> oldViewCounts = viewCountRepository.findAll();
                for (ViewCount viewCount : oldViewCounts) {
                    String filename = viewCount.getRequestURI().replace("/static/", "");
                    s3StorageService.deleteImageFromS3(baseUrl + filename);
                }
                viewCountRepository.deleteAll();

                // 새 이미지 S3에 추가
                //
                for (ViewCount viewCount : viewCounts) {

                    try {
                        String filename = viewCount.getRequestURI().replace("/static/", "");
                        s3StorageService.storeFile(
                            downloadImageAsMultipartFile(filename));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                viewCountRepository.saveAll(viewCounts);
                sink.next("Buckets saved successfully!");
            }).block();
    }

    public MultipartFile downloadImageAsMultipartFile(String imageUrl) throws IOException {
        // 이미지 다운로드
        URL url = new URL(baseUrl + imageUrl);
        InputStream inputStream = url.openStream();

        // 이미지 바이트 배열로 변환
        byte[] imageBytes = inputStream.readAllBytes();

        // 원본 파일명 생성 (URL에서 파일명 추출)
        String originalFilename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        return new CustomMultipartFile(
            imageBytes,
            originalFilename,
            "image/jpeg"
        );
    }
}
