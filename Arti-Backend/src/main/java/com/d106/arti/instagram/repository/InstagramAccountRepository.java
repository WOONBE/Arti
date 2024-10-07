package com.d106.arti.instagram.repository;

import com.d106.arti.instagram.domain.InstagramAccount;
import com.d106.arti.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstagramAccountRepository extends JpaRepository<InstagramAccount, Integer> {

    Optional<InstagramAccount> findByMember(Member member);
}