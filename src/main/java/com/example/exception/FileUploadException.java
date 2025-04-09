package com.example.exception;

public class FileUploadException extends Exception {
    private final FileUploadErrorType errorType;

    public enum FileUploadErrorType {
        FILE_NOT_FOUND,
        FILE_SIZE_EXCEEDED,
        UNSUPPORTED_FORMAT
    }

    public FileUploadException(String message, FileUploadErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public FileUploadException(String message, FileUploadErrorType errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public FileUploadErrorType getErrorType() {
        return errorType;
    }
}
