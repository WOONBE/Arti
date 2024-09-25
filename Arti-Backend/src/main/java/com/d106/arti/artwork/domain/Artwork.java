package com.d106.arti.artwork.domain;



import com.d106.arti.gallery.domain.Theme;
import com.d106.arti.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "ARTIST_ID")
    private Artist artist;

    // ArtworkTheme과 1:N 관계
    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtworkTheme> artworkThemes = new ArrayList<>();

    // 테마 추가 편의 메서드
    public void addTheme(Theme theme, String description) {
        ArtworkTheme artworkTheme = ArtworkTheme.builder()
            .artwork(this)
            .theme(theme)
            .description(description)
            .build();
        artworkThemes.add(artworkTheme);
        theme.getArtworks().add(artworkTheme);
    }

    // 테마 삭제 편의 메서드
    public void removeTheme(Theme theme) {
        artworkThemes.removeIf(artworkTheme -> artworkTheme.getTheme().equals(theme));
        theme.getArtworks().removeIf(artworkTheme -> artworkTheme.getArtwork().equals(this));

    }

    // Artist 설정 메서드
    public void updateArtist(Artist artist) {
        this.artist = artist;
    }





}
