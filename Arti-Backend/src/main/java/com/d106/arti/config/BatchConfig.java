package com.d106.arti.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobBuilderHelper;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job importArtworkJob(Step artworkStep) {
        return new JobBuilder("importArtworkJob", jobRepository)
                .start(artworkStep)
                .build();
    }

    @Bean
    public Job importArtistJob(Step artistStep) {
        return new JobBuilder("importArtistJob", jobRepository)
                .start(artistStep)
                .build();
    }

    public StepBuilder stepBuilder(String name) {
        return new StepBuilder(name, jobRepository);
    }
}
