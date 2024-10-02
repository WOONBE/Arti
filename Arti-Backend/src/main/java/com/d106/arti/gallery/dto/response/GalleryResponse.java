package com.d106.arti.gallery.dto.response;

import com.d106.arti.gallery.domain.Gallery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryResponse {

    private Integer id;
    private String name;
    private String description;
    private String image;
    private Integer viewCount;
    private Integer ownerId;

    // Entity를 DTO로 변환하는 메서드
    public static GalleryResponse fromEntity(Gallery gallery) {
        return new GalleryResponse(
                gallery.getId(),
                gallery.getName(),
                gallery.getDescription(),
                gallery.getImage(),
                gallery.getView(),
                gallery.getOwner().getId()
        );
    }
}
