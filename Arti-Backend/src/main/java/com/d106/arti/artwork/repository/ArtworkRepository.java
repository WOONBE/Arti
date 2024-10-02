package com.d106.arti.artwork.repository;


import com.d106.arti.artwork.domain.NormalArtWork;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtworkRepository extends JpaRepository<NormalArtWork, Integer>, ArtworkRepositoryCustom  {

    @Query("SELECT a FROM NormalArtWork a WHERE a.genre LIKE %?1%")
    List<NormalArtWork> findAllByGenreContaining(String genre);

}
