package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery,Integer> {
    List<Gallery> findAllByIdIn(List<Integer> galleryIds);

    List<Gallery> findByNameContaining(String keyword);

    Optional<Gallery> findFirstByOwner(Member owner);

}
