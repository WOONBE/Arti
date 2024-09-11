package com.d106.arti.gallery;

import com.d106.arti.artwork.domain.AiArtwork;
import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.ArtworkTheme;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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


//    //미술품과 1:n
//    // 테마와 AiArtWork는 1:N 관계
//    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<AiArtwork> artworks = new ArrayList<>();
//
//    // AiArtWork 추가 편의 메서드
//    public void addArtwork(AiArtwork artwork) {
//        artworks.add(artwork);
//        artwork.updateTheme(this);
//    }
//
//    // AiArtWork 삭제 편의 메서드
//    public void removeArtwork(AiArtwork artwork) {
//        artworks.remove(artwork);
//        artwork.updateTheme(null);
//    }
    // ArtworkTheme과 1:N 관계
    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ArtworkTheme> artworkThemes = new ArrayList<>();

        // 편의 메서드
        public void addArtwork(Artwork artwork, String description) {
            ArtworkTheme artworkTheme = ArtworkTheme.builder()
                .artwork(artwork)
                .theme(this)
                .description(description)
                .build();
            artworkThemes.add(artworkTheme);
        }

        public void removeArtwork(Artwork artwork) {
            artworkThemes.removeIf(artworkTheme -> artworkTheme.getArtwork().equals(artwork));
        }


}
