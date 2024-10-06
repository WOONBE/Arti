package com.d106.arti.member.repository;

import com.d106.arti.member.domain.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthTokenRepository extends JpaRepository<OauthToken, Integer> {

    // memberId로 OauthToken 조회
    Optional<OauthToken> findByMemberId(Integer memberId);
}