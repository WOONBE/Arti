package com.d106.arti.gallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.d106.arti.gallery.domain.Gallery;

import java.util.Optional;
// '병현의 미술관 -<내 미술관 페이지> 고려..
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
    Optional<Gallery> findByIdAndIsDeletedFalse(Integer galleryId);

}
