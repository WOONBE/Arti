package com.d106.arti.gallery.domain;

import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;

import java.util.List;
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
import com.d106.arti.gallery.domain.GalleryViewRecord;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL)
    private List<GalleryViewRecord> viewRecords;

    // 소유인(Member)과의 ManyToOne 관계 설정
    @ManyToOne
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Member owner;

    // 지난 7일간의 조회수를 계산하는 메서드
    public Integer calculateWeeklyView() {
        // LocalDate.now(): 현재 날짜를 반환. 'today' 변수에 현재 날짜를 저장
        LocalDate today = LocalDate.now();

        // 오늘 날짜에서 7일을 뺀 날짜를 구하여 'sevenDaysAgo' 변수에 저장. 즉, 지난 7일의 시작 날짜를 구함.
        LocalDate sevenDaysAgo = today.minusDays(7);

        // viewRecords 리스트를 스트림으로 변환하여 필터링 및 합계 작업을 수행
        // viewRecords.stream(): viewRecords 리스트를 스트림(Stream) 형태로 변환. 스트림은 리스트 등의 데이터 구조를 처리하기 위한 API
        return viewRecords.stream()

                // .filter(record -> ...): 스트림 내 각 record에 대해 조건을 만족하는지 검사. true인 항목만 다음 단계로 넘김.
                // !record.getViewDate().isBefore(sevenDaysAgo): record의 날짜가 지난 7일보다 이전 날짜가 아닌 경우만 필터링
                // !record.getViewDate().isAfter(today): record의 날짜가 오늘 이후 날짜가 아닌 경우만 필터링
                .filter(record -> !record.getViewDate().isBefore(sevenDaysAgo) && !record.getViewDate().isAfter(today))

                // .mapToInt(GalleryViewRecord::getViewCount): 필터링된 record에서 getViewCount() 값을 가져와 int형으로 변환. 이 변환 작업은 스트림에서 수행됨.
                .mapToInt(GalleryViewRecord::getViewCount)

                // .sum(): 변환된 int 값들을 모두 더하여 합계를 반환
                .sum();
    }

    // 이미지가 없으면 기본 이미지 또는 "이미지가 없습니다" 문구를 반환
    public String getImage() {
        if (image == null || image.isEmpty()) {
            return "default-image-url.jpg"; // (Arti 이미지 넣을 예정!) 기본 이미지 URL
        }
        return image;
    }
}
