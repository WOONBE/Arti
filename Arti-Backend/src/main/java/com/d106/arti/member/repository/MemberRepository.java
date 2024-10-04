package com.d106.arti.member.repository;

import com.d106.arti.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

}
