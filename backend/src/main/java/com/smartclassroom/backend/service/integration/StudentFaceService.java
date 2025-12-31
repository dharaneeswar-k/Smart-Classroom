package com.smartclassroom.backend.service.integration;

import org.springframework.web.multipart.MultipartFile;

public interface StudentFaceService {
    void uploadFaceImage(String studentId, MultipartFile file);
}
