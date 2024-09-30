package com.d106.arti.global.exception;

import lombok.Getter;

@Getter
public class S3Exception extends RuntimeException {

    private final int code;
    private final String message;

    public S3Exception(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
