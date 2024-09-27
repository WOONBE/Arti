package com.d106.arti.artwork.repository;

import com.d106.arti.artwork.domain.NormalArtWork;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ArtworkRepositoryCustom {
    Page<NormalArtWork> search(String keyword, Pageable pageable);

}
