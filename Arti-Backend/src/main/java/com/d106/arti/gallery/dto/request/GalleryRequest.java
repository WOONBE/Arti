package com.d106.arti.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryRequest {

    private String name;
    private String description;
    private String image;
    private Integer ownerId;  // Member ID 참조
}
