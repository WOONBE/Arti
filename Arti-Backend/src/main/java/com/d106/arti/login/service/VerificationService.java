package com.d106.arti.login.service;

import com.d106.arti.login.domain.Verification;
import com.d106.arti.login.repository.VerificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class VerificationService {

    private final VerificationRepository verificationCodeRepository;

    public VerificationService(VerificationRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    public void saveVerificationCode(String email, String code) {
        Verification verificationCode = new Verification();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryDate(LocalDateTime.now().plusMinutes(10)); // 10분 유효
        verificationCodeRepository.save(verificationCode);
    }

    public boolean verifyCode(String email, String code) {
        Verification storedCode = verificationCodeRepository.findById(email).orElse(null);
        if (storedCode == null || storedCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return storedCode.getCode().equals(code);
    }

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    public void deleteExpiredCodes() {
        verificationCodeRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}
