package com.d106.arti;

import com.d106.arti.auth.AuthenticationService;
import com.d106.arti.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.d106.arti.member.domain.Role.ADMIN;
import static com.d106.arti.member.domain.Role.MANAGER;


@EnableScheduling // 인증번호 저장 및 만료를 위해(mysql사용)
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
@EnableAsync
public class ArtiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtiApplication.class, args);
    }

}
