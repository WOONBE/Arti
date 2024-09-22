package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    // 특정 회원이 소유한 미술관 목록 조회
    List<Gallery> findByOwner(Member owner);

    // 특정 회원이 소유한 미술관 삭제
    void deleteByOwner(Member owner);

    // 미술관 이름으로 검색하는 메서드 (부분 일치)
    @Query("SELECT g FROM Gallery g WHERE g.galleryTitle LIKE %:name%")
    List<Gallery> findByNameContaining(@Param("name") String name);

    // 테마 이름을 통한 미술관 검색
    @Query("SELECT g FROM Gallery g JOIN g.themes t WHERE t.themeTitle LIKE %:theme%")
    List<Gallery> findByThemeContaining(@Param("theme") String theme);
}

