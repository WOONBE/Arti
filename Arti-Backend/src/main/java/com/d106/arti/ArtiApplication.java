package com.d106.arti;

import com.d106.arti.auth.AuthenticationService;
import com.d106.arti.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.d106.arti.member.domain.Role.ADMIN;
import static com.d106.arti.member.domain.Role.MANAGER;


@EnableScheduling // 인증번호 저장 및 만료를 위해(mysql사용)
@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
public class ArtiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtiApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(
//            AuthenticationService service
//    ) {
//        return args -> {
//            var admin = RegisterRequest.builder()
//                    .email("admin@mail.com")
//                    .password("password")
//                    .nickname("admin")
//                    .role(ADMIN)
//                    .build();
//            System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//            var manager = RegisterRequest.builder()
//                    .email("manager@mail.com")
//                    .password("password")
//                    .nickname("manager")
//                    .role(MANAGER)
//                    .build();
//            System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//        };
//    }
}
