package com.d106.arti.artwork.repository;

import com.d106.arti.artwork.domain.NormalArtWork;

import java.util.List;

public interface ArtworkRepositoryCustom {
    List<NormalArtWork> search(String keyword);
}
