package com.smartclassroom.backend.service.integration;

import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@lombok.extern.slf4j.Slf4j
public class StudentFaceServiceImpl implements StudentFaceService {

    @Autowired
    private StudentRepository studentRepository;

    private static final String UPLOAD_DIR = "d:\\CODING STUFFS\\Smart-Classroom\\ai\\known_faces";

    @Override
    public void uploadFaceImage(String studentIdStr, MultipartFile file) {
        log.info("Starting face image upload for studentIdStr: {}", studentIdStr);
        Long studentId;
        try {
            studentId = Long.parseLong(studentIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid student ID format: {}", studentIdStr);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student ID format");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("Student not found with ID: {}", studentId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
                });

        if (file.isEmpty()) {
            log.error("Uploaded file is empty for student ID: {}", studentId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        try {
            // Ensure directory exists (just in case)
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                log.info("Created directory: {} -> {}", UPLOAD_DIR, created);
            }

            // Save file as student_{id}.jpg (assuming jpg or strictly enforcing extensions
            // later if needed)
            // Ideally we keep original extension or convert. For AI script consistency,
            // let's keep it simple.
            // Script often expects image files.

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = ".jpg"; // Default
            }

            String filename = "student_" + student.getId() + extension;
            Path filePath = Paths.get(UPLOAD_DIR, filename);

            log.info("Saving file to: {}", filePath);
            file.transferTo(filePath.toFile());

            // Update student record
            student.setFaceImagePath(filePath.toString());
            studentRepository.save(student);
            log.info("Successfully updated student {} with face image path", student.getId());

        } catch (IOException e) {
            log.error("Failed to store file for student {}: {}", student.getId(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", e);
        }
    }
}
