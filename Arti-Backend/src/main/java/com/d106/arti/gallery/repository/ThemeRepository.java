package com.d106.arti.gallery.repository;



import com.d106.arti.gallery.domain.Theme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Integer> {
    List<Theme> findByGalleryId(Integer galleryId);

}
