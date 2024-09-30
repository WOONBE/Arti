package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Gallery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery,Integer> {
    List<Gallery> findAllByIdIn(List<Integer> galleryIds);

    List<Gallery> findByNameContaining(String keyword);

}
