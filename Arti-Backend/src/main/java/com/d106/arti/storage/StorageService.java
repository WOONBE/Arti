package com.d106.arti.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String getFullPath(String filename);

    UploadFile storeFile(MultipartFile multipartFile);

    void deleteFile(String filename);
}
