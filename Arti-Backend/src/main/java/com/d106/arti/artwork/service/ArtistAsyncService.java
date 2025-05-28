package com.d106.arti.artwork.service;

import com.d106.arti.artwork.domain.Artist;
import com.d106.arti.artwork.repository.ArtistRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistAsyncService {

    private final ArtistRepository artistRepository;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<Artist>> findByEngNameContaining(String keyword) {
        return CompletableFuture.completedFuture(artistRepository.findByEngNameContaining(keyword));
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<Artist>> findByKorNameContaining(String keyword) {
        return CompletableFuture.completedFuture(artistRepository.findByKorNameContaining(keyword));
    }
}
