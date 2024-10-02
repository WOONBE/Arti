package com.d106.arti.artwork.domain;



import com.d106.arti.gallery.domain.Theme;
import jakarta.persistence.*;
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
@Table(name = "artwork_theme")
public class ArtworkTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    // Artwork와 N:1 관계
    @ManyToOne
    @JoinColumn(name = "ARTWORK_ID", nullable = false)
    private Artwork artwork;

    // Theme와 N:1 관계
    @ManyToOne
    @JoinColumn(name = "THEME_ID", nullable = false)
    private Theme theme;


    // 예를 들어, 이 테마에서 미술품의 순서 또는 역할 등을 나타내는 필드
    @Column(name = "description")
    private String description;

    // 편의 메서드: Artwork와 Theme 설정
    public void updateArtworkAndTheme(Artwork artwork, Theme theme) {
        this.artwork = artwork;
        this.theme = theme;
        artwork.getArtworkThemes().add(this);  // Artwork에 추가
        theme.getArtworks().add(this);         // Theme에 추가
    }
}
