package com.d106.arti.artwork.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ThemeArtworkId implements Serializable {

    private Integer artworkId;
    private Integer themeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThemeArtworkId that = (ThemeArtworkId) o;
        return Objects.equals(artworkId, that.artworkId) && Objects.equals(themeId, that.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artworkId, themeId);
    }
}
