package com.d106.arti.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping("/")
    public ResponseEntity<String> storeFile(@RequestParam("file") MultipartFile file) {
        UploadFile uploadFile = storageService.storeFile(file);
        return ResponseEntity.ok(uploadFile.getStoreFilename());
    }
}
