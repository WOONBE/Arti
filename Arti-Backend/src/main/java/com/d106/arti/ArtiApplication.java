package com.d106.arti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 인증번호 저장 및 만료를 위해(mysql사용)
@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
public class ArtiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtiApplication.class, args);
    }

}
