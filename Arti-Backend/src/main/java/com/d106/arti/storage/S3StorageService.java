package com.d106.arti.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.d106.arti.global.exception.ExceptionCode;
import com.d106.arti.global.exception.S3Exception;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Override
    public String getFullPath(String filename) {
        return "";
    }

    @Override
    public UploadFile storeFile(MultipartFile multipartFile) {
        return UploadFile.builder()
            .uploadFilename(multipartFile.getOriginalFilename())
            .storeFilename(upload(multipartFile))
            .build();
    }

    @Override
    public void deleteFile(String filename) {
        deleteImageFromS3(filename);
    }

    private String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new S3Exception(ExceptionCode.EMPTY_FILE_EXCEPTION);
        }
        return this.uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new S3Exception(ExceptionCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Exception(ExceptionCode.NO_FILE_EXTENTION);
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new S3Exception(ExceptionCode.INVALID_FILE_EXTENTION);
        }
    }

    //    private String uploadImageToS3(MultipartFile image) throws IOException {
//        String originalFilename = image.getOriginalFilename(); //원본 파일 명
//        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명
//
//        String s3FileName =
//            UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명
//
//        InputStream is = image.getInputStream();
//        byte[] bytes = IOUtils.toByteArray(is);
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType("image/" + extention);
//        metadata.setContentLength(bytes.length);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//
//        try {
//            PutObjectRequest putObjectRequest =
//                new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
//                    .withCannedAcl(CannedAccessControlList.PublicRead);
//            amazonS3.putObject(putObjectRequest); // put image to S3
//        } catch (Exception e) {
//            throw new S3Exception(ExceptionCode.PUT_OBJECT_EXCEPTION);
//        } finally {
//            byteArrayInputStream.close();
//            is.close();
//        }
//
//        return amazonS3.getUrl(bucketName, s3FileName).toString();
//    }
    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); // 원본 파일 명
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
            .toLowerCase(); // 확장자 명

        // UUID로 랜덤한 파일명 생성
//        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);

        // 확장자에 따른 Content-Type 설정
        switch (extension) {
            case "jpg":
            case "jpeg":
                metadata.setContentType("image/jpeg");
                break;
            case "png":
                metadata.setContentType("image/png");
                break;
            case "gif":
                metadata.setContentType("image/gif");
                break;
            default:
                throw new S3Exception(
                    ExceptionCode.INVALID_FILE_EXTENTION); // 지원되지 않는 확장자일 경우 예외 처리
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, originalFilename,
                byteArrayInputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead); // Public 읽기 권한 부여
            amazonS3.putObject(putObjectRequest); // S3에 이미지 업로드
        } catch (Exception e) {
            throw new S3Exception(ExceptionCode.PUT_OBJECT_EXCEPTION);
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, originalFilename).toString(); // 업로드된 파일 URL 반환
    }

    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new S3Exception(ExceptionCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new S3Exception(ExceptionCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }
}
