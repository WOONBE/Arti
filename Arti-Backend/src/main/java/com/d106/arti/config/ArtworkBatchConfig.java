package com.d106.arti.config;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.repository.ArtistRepository;
import com.d106.arti.artwork.repository.ArtworkRepository;
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
import java.util.Objects;
import java.util.stream.*;

@Configuration
@RequiredArgsConstructor
public class ArtworkBatchConfig {


    private final ArtworkRepository artworkRepository;
    private final ArtistRepository artistRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public FlatFileItemReader<NormalArtWork> artworkReader() {
        return new FlatFileItemReaderBuilder<NormalArtWork>()
                .name("artworkReader")
                .resource(new ClassPathResource("artworks.csv"))
                .linesToSkip(1)
                .delimited()
                .names("filename", "artistName", "genre", "description", "phash", "width", "height", "genreCount", "subset", "artistKo", "title", "year")
                .fieldSetMapper(fieldSet -> {
                    String artistName = fieldSet.readString("artistName");
                    Artist artist = artistRepository.findByEngName(artistName);
                    if (artist == null) return null;

                    NormalArtWork artwork = NormalArtWork.builder()
                            .filename(fieldSet.readString("filename"))
                            .artistName(artistName)
                            .genre(fieldSet.readString("genre"))
                            .description(fieldSet.readString("description"))
                            .phash(fieldSet.readString("phash"))
                            .width(parseInt(fieldSet.readString("width")))
                            .height(parseInt(fieldSet.readString("height")))
                            .genreCount(parseInt(fieldSet.readString("genreCount")))
                            .subset(fieldSet.readString("subset"))
                            .artistKo(fieldSet.readString("artistKo"))
                            .title(fieldSet.readString("title"))
                            .year(fieldSet.readString("year"))
                            .build();
                    artwork.updateArtist(artist);
                    return artwork;
                })
                .build();
    }

    @Bean
    public ItemWriter<NormalArtWork> artworkWriter() {
        return items -> {
            List<NormalArtWork> filtered = new ArrayList<>();
            for (NormalArtWork item : items) {
                if (item != null) {
                    filtered.add(item);
                }
            }
            artworkRepository.saveAll(filtered);
        };
    }

    @Bean
    public Step artworkStep() {
        return new StepBuilder("artworkStep", jobRepository)
                .<NormalArtWork, NormalArtWork>chunk(100, transactionManager)
                .reader(artworkReader())
                .writer(artworkWriter())
                .build();
    }

    private int parseInt(String value) {
        try {
            return value == null || value.isBlank() ? 0 : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
