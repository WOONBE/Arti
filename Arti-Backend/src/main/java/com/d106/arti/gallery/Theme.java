package com.d106.arti.gallery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Theme {

    @Id
    @Column(name = "THEME_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //미술관과 n : 1

    //미술품과 1:n


}
