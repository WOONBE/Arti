package com.d106.arti.gallery.domain;

import com.d106.arti.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GALLERY_ID")
    private Integer id;

    @Column(name = "GALLERY_TITLE", nullable = false)
    private String galleryTitle;

    @OneToOne
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Member owner;

    @Column(name = "GALLERY_IMG")
    private String image;

    @Column(name = "GALLERY_DESC")
    private String description;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Theme> themes;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;

    // 기본 이미지 반환 메서드 수정됨
    public String getImage() {
        if (image == null || image.isEmpty()) {
            return "https://i.namu.wiki/i/rBUhEykiWxuRzyi24BF8xXQCvEH2tI-LQLMUPg2JCvCLQW9Z1epKU9whqRRf4PUJIxOsLTUrlqFssvUuxqPoww4FOj6z_gUXhUmRTRhyKrukL6Gw_rPyoIwEhm9PhjEkwnsEfMirAcnBHfDXmiDyLA.jpg"; // 수정됨: 기본 이미지 추가
        }
        return image;
    }
    public void setGalleryTitle(String newTitle) {
        this.galleryTitle = newTitle;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
