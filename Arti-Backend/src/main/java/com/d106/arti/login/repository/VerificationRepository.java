package com.d106.arti.login.repository;


import com.d106.arti.login.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface VerificationRepository extends JpaRepository<Verification, String> {

    void deleteAllByExpiryDateBefore(LocalDateTime now);
}