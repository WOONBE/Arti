package com.d106.arti.artwork.repository;

import com.d106.arti.artwork.domain.Artist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    // 영어 이름으로 부분 검색
    @Query("SELECT a FROM Artist a WHERE a.engName LIKE %:engName%")
    List<Artist> findByEngNameContaining(@Param("engName") String engName);

    // 한국어 이름으로 부분 검색
    @Query("SELECT a FROM Artist a WHERE a.korName LIKE %:korName%")
    List<Artist> findByKorNameContaining(@Param("korName") String korName);

    // 요약으로 부분 검색
    @Query("SELECT a FROM Artist a WHERE a.summary LIKE %:summary%")
    List<Artist> findBySummaryContaining(@Param("summary") String summary);

    Artist findByEngName(String engName);

    List<Artist> findAll();


//    List<Artist> findByEngNameContainingOrKorNameContainingOrSummaryContaining(
//        String engName, String korName, String summary
//    );
}
