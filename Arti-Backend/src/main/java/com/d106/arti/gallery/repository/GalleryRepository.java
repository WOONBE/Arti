package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    // 특정 회원이 소유한 미술관 목록을 조회하는 메서드
    List<Gallery> findByOwner(Member owner);

    // 특정 회원이 소유한 미술관을 삭제하는 메서드
    void deleteByOwner(Member owner);
    //
//    // 미술관 이름으로 검색하는 메서드 (부분 일치)
//    @Query("SELECT g FROM Gallery g WHERE g.name LIKE %:name%")
//    List<Gallery> findByNameContaining(@Param("name") String name);
//
//    // 수정된 부분: Theme과의 관계를 이용하여 조인 후 검색
//    @Query("SELECT g FROM Gallery g JOIN g.themes t WHERE t.name LIKE %:theme%")
//    List<Gallery> findByThemeContaining(@Param("theme") String theme);
}