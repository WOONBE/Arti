package com.d106.arti.artwork.repository;

import com.d106.arti.artwork.domain.AiArtwork;
import com.d106.arti.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiArtworkRepository extends JpaRepository<AiArtwork, Integer> {
    List<AiArtwork> findByMember(Member member);
}
