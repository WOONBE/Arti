package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery,Integer> {

}
