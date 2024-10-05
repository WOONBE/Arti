package com.d106.arti.member.repository;

import com.d106.arti.member.domain.InstagramAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstagramAccountRepository extends JpaRepository<InstagramAccount, Integer> {

    // Instagram 이메일로 계정 검색
    Optional<InstagramAccount> findByInstagramEmail(String email);

    // 이메일 중복 여부 확인
    boolean existsByInstagramEmail(String email);
}