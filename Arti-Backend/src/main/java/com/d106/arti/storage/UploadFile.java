package com.d106.arti.storage;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadFile {

    private String uploadFilename;
    private String storeFilename;
}