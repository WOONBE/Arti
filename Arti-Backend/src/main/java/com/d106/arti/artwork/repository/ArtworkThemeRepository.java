package com.d106.arti.artwork.repository;

import com.d106.arti.artwork.domain.ArtworkTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkThemeRepository extends JpaRepository<ArtworkTheme, Long> {
}