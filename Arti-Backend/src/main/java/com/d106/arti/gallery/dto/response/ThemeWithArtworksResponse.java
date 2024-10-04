package com.d106.arti.gallery.dto.response;

import com.d106.arti.artwork.dto.response.ArtworkResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThemeWithArtworksResponse {
    private Integer themeId;
    private String themeName;
    private List<ArtworkResponse> artworks;
}
