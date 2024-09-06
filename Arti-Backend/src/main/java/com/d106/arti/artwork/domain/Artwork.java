package com.d106.arti.artwork.domain;

import com.d106.arti.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(
    name = "artwork",
    indexes = @Index(name = "idx_artwork_id", columnList = "ARTWORK_ID")
)
@DiscriminatorColumn(name = "ARTWORK_TYPE") // 구분할 컬럼 추가
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Artwork extends BaseEntity {

    @Id
    @Column(name = "ARTWORK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "ARTWORK_YEAR")
//    private Integer year;
//
//    @Column(name = "ARTWORK_TITLE")
//    private String title;
//
//    @Column(name = "ARTWORK_IMAGE")
//    private String image;
//
//    @Column(name = "ARTWORK_DESC")
//    private String description;






}
