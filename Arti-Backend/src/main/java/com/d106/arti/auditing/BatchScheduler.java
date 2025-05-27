package com.d106.arti.auditing;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job importArtworkJob;
    private final Job importArtistJob;

    @Scheduled(cron = "0 0 12 * * *") // 매일 정오
    public void runBatchJobs() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // 중복 실행 방지
                .toJobParameters();

        jobLauncher.run(importArtistJob, parameters);
        jobLauncher.run(importArtworkJob, parameters);
    }
}
