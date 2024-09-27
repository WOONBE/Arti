package com.d106.arti.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryRequest {

    private String name;
    private String description;
    private Integer ownerId;  // Member ID 참조

}
