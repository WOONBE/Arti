package com.d106.arti.config;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ArtistBatchConfig {

    private final ArtistRepository artistRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public FlatFileItemReader<Artist> artistReader() {
        return new FlatFileItemReaderBuilder<Artist>()
                .name("artistReader")
                .resource(new ClassPathResource("artists.csv"))
                .linesToSkip(1)
                .delimited()
                .names("engName", "korName", "summary", "image")
                .fieldSetMapper(fieldSet -> Artist.builder()
                        .engName(fieldSet.readString("engName"))
                        .korName(fieldSet.readString("korName"))
                        .summary(fieldSet.readString("summary"))
                        .image(fieldSet.readString("image"))
                        .build())
                .build();
    }

//    @Bean
//    public ItemWriter<Artist> artistWriter() {
//        return artistRepository::saveAll;
//    }
    @Bean
    public ItemWriter<Artist> artistWriter() {
        return items -> {
            List<Artist> filtered = new ArrayList<>();
            for (Artist artist : items) {
                if (artist == null) continue;
                if (artistRepository.existsByKorName(artist.getEngName())) continue; // 중복 체크
                filtered.add(artist);
            }
            artistRepository.saveAll(filtered);
        };
    }

    @Bean
    public Step artistStep() {
        return new StepBuilder("artistStep", jobRepository)
                .<Artist, Artist>chunk(100, transactionManager)
                .reader(artistReader())
                .writer(artistWriter())
                .build();
    }
}
