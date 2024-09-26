package com.d106.arti.artwork.repository;


import com.d106.arti.artwork.domain.NormalArtWork;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtworkRepository extends JpaRepository<NormalArtWork, Integer>, ArtworkRepositoryCustom  {
    List<NormalArtWork> findAllByGenreContaining(String genre);

}
