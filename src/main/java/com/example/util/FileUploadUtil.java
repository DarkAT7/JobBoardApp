package com.example.util;

import com.example.exception.FileUploadException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUploadUtil {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList(".pdf", ".doc", ".docx");
    private static final String UPLOAD_DIR = "uploads/resumes";

    public static String uploadResume(String filePath) throws FileUploadException {
        try {
            File file = new File(filePath);
            
            // Check if file exists
            if (!file.exists()) {
                throw new FileUploadException(
                    "File not found: " + filePath,
                    FileUploadException.FileUploadErrorType.FILE_NOT_FOUND
                );
            }

            // Check file size
            if (file.length() > MAX_FILE_SIZE) {
                throw new FileUploadException(
                    "File size exceeds maximum limit of 5MB",
                    FileUploadException.FileUploadErrorType.FILE_SIZE_EXCEEDED
                );
            }

            // Check file format
            String fileExtension = getFileExtension(file.getName()).toLowerCase();
            if (!SUPPORTED_FORMATS.contains(fileExtension)) {
                throw new FileUploadException(
                    "Unsupported file format. Supported formats are: " + String.join(", ", SUPPORTED_FORMATS),
                    FileUploadException.FileUploadErrorType.UNSUPPORTED_FORMAT
                );
            }

            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String uniqueFileName = System.currentTimeMillis() + "_" + file.getName();
            Path destinationPath = uploadPath.resolve(uniqueFileName);

            // Copy file to upload directory
            Files.copy(file.toPath(), destinationPath);

            return destinationPath.toString();
        } catch (IOException e) {
            throw new FileUploadException(
                "Error uploading file: " + e.getMessage(),
                FileUploadException.FileUploadErrorType.FILE_NOT_FOUND,
                e
            );
        }
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
}
