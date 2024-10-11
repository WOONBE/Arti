package com.d106.arti.gallery.domain;

import com.d106.arti.global.common.BaseEntity;
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

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Gallery extends BaseEntity {

    @Id
    @Column(name = "GALLERY_ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "GALLERY_TITLE")
    private String name;

    @Column(name = "GALLERY_VIEW")
    private Integer view;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Theme> themes;


    // 소유인(Member)과의 1:1 관계 설정(수정)
    // 갤러리 객체가 누구에 의해 소유되었는지, owner 필드는 Member 객체를 참조하는 것으로, 갤러리의 소유자가 누구인지
    @OneToOne
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Member owner;

    @Column(name = "GALLERY_IMG")
    private String image;

    @Column(name = "GALLERY_DESC")
    private String description;

    // 연관관계 편의 메서드: Theme 추가
    public void addTheme(Theme theme) {
        themes.add(theme);
        theme.updateGallery(this);  // 반대편도 설정
    }

    // 연관관계 편의 메서드: Theme 삭제
    public void removeTheme(Theme theme) {
        themes.remove(theme);
        theme.updateGallery(null);// 반대편의 참조 해제
    }



//    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL)
//    private List<GalleryViewRecord> viewRecords;






}
