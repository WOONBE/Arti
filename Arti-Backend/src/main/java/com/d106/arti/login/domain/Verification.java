package com.d106.arti.login.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Verification {

    @Id
    private String email;
    private String code;
    private LocalDateTime expiryDate;

}
