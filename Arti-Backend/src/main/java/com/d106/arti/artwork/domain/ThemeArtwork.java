package com.d106.arti.artwork.domain;

import com.d106.arti.gallery.domain.Theme;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "theme_artwork", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"artwork_id", "theme_id"})
})
public class ThemeArtwork implements Serializable {

    @EmbeddedId
    private ThemeArtworkId id;

    // Artwork와 다대일 관계 설정 (변경됨: cascade나 orphanRemoval 사용하지 않음)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artworkId")
    @JoinColumn(name = "artwork_id", nullable = false)
    private Artwork artwork;

    // Theme와 다대일 관계 설정 (변경됨: orphanRemoval = true 사용)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("themeId")
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    // 생성자: Artwork와 Theme를 인자로 받아 ThemeArtwork 객체를 생성
    public ThemeArtwork(Artwork artwork, Theme theme) {
        this.id = new ThemeArtworkId(artwork.getId(), theme.getId());
        this.artwork = artwork;
        this.theme = theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThemeArtwork that = (ThemeArtwork) o;
        return Objects.equals(artwork.getId(), that.artwork.getId()) &&
                Objects.equals(theme.getId(), that.theme.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(artwork.getId(), theme.getId());
    }
}
