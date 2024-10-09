package com.d106.arti.elasticsearch.repository;

import com.d106.arti.elasticsearch.entity.ViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewCountRepository extends JpaRepository<ViewCount, Long> {

    Boolean existsByRequestURI(String requestURI);
}
