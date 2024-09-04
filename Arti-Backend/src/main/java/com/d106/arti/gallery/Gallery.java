package com.d106.arti.gallery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import javax.print.attribute.standard.MediaSize.NA;
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
public class Gallery {

    @Id
    @Column(name = "GALLERY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "GALLERY_NAME")
    private String name;

    @Column(name = "GALLERY_VIEW")
    private Integer view;

    @Column(name = "GALLERY_IMG")
    private String image;

    @Column(name = "GALLERY_DESC")
    private String description;

}
