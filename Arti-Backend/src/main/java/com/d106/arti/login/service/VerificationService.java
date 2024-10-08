package com.d106.arti.login.service;

import com.d106.arti.login.domain.Verification;
import com.d106.arti.login.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;



@Service
public class VerificationService {

    @Autowired
    private final VerificationRepository verificationRepository;

    public VerificationService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    public void saveVerificationCode(String email, String code) {
        Verification verificationCode = new Verification();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(10));  // 10분 후 만료
        verificationRepository.save(verificationCode);
    }

    public boolean verifyCode(String email, String code) {
        Verification storedCode = verificationRepository.findById(email).orElse(null);
        if (storedCode == null || storedCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;  // 코드가 없거나 만료되었으면 실패
        }
        return storedCode.getCode().equals(code);  // 코드가 일치하면 성공
    }

    public boolean isVerifiedEmail(String email) {
        Verification verification = verificationRepository.findById(email).orElse(null);
        return verification != null && verification.getExpiryDate().isAfter(LocalDateTime.now());  // 이메일이 존재하고 만료되지 않은 경우
    }

    @Transactional  // 트랜잭션을 보장
    @Scheduled(fixedRate = 600000)  // 10분 간격으로 실행
    public void deleteExpiredCodes() {
        verificationRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}
