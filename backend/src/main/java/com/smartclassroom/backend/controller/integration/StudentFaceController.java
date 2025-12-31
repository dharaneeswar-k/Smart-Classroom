package com.smartclassroom.backend.controller.integration;

import com.smartclassroom.backend.service.integration.StudentFaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentFaceController {

    private final StudentFaceService studentFaceService;

    @PostMapping(value = "/{studentId}/face", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFaceImage(
            @PathVariable String studentId,
            @RequestParam("file") MultipartFile file) {
        studentFaceService.uploadFaceImage(studentId, file);
        return ResponseEntity.ok().build();
    }
}
