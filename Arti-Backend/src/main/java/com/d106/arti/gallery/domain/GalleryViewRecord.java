package com.d106.arti.gallery.domain;
import jakarta.persistence.*;
import lombok.*;
// (수정!)나중에 특정 파일 import로 수정하기!
import java.time.LocalDate;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class GalleryViewRecord {
    @Id
    @Column(name = "GALLERY_VIEW_RECORD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "GALLERY_ID")
    private Gallery gallery;

    @Column(name = "GALLERY_VIEW_INIT_DATE")
    private LocalDate viewDate;  // 조회수 발생일

    @Column(name = "GALLERY_VIEW_COUNT")
    private Integer viewCount;   // 해당 날짜의 조회수
}