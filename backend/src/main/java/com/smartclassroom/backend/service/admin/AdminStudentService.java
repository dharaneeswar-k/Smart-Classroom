package com.smartclassroom.backend.service.admin;

import com.smartclassroom.backend.dto.admin.StudentDTO;
import com.smartclassroom.backend.model.Classroom;
import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.model.User;
import com.smartclassroom.backend.repository.ClassroomRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import com.smartclassroom.backend.service.UserProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminStudentService {

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private ClassroomRepository classroomRepository;

        @Autowired
        private UserProvisioningService userProvisioningService;

        @Transactional
        public StudentDTO createStudent(StudentDTO dto, org.springframework.web.multipart.MultipartFile image) {
                // 1. Validate Classroom
                Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND,
                                                "Classroom not found with ID: " + dto.getClassroomId()));

                // 2. Create User via Provisioning Service
                String password = (dto.getPassword() != null && !dto.getPassword().isEmpty()) ? dto.getPassword()
                                : "student123";
                User user;
                try {
                        user = userProvisioningService.createUser(
                                        dto.getStudentRollNo(),
                                        password,
                                        dto.getEmail(),
                                        "STUDENT");
                } catch (RuntimeException e) {
                        if (e.getMessage() != null && e.getMessage().contains("Username already exists")) {
                                throw new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.CONFLICT,
                                                "User with Roll No " + dto.getStudentRollNo() + " already exists.");
                        }
                        throw e;
                }

                // 3. Handle specific logic for file upload
                String faceImagePath = null;
                if (image != null && !image.isEmpty()) {
                        try {
                                String folderPath = System.getProperty("user.dir") + "\\ai\\known_faces";
                                java.nio.file.Path uploadDir = java.nio.file.Paths.get(folderPath);
                                if (!java.nio.file.Files.exists(uploadDir)) {
                                        java.nio.file.Files.createDirectories(uploadDir);
                                }

                                String originalFilename = image.getOriginalFilename();
                                String fileExtension = "";
                                if (originalFilename != null && originalFilename.contains(".")) {
                                        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                                } else {
                                        fileExtension = ".jpg"; // Default
                                }

                                String filename = dto.getStudentRollNo() + fileExtension;
                                java.nio.file.Path filePath = uploadDir.resolve(filename);
                                java.nio.file.Files.copy(image.getInputStream(), filePath,
                                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                                // Store relative path (corrected to forward slashes for compatibility)
                                faceImagePath = "ai/known_faces/" + filename;

                        } catch (java.io.IOException e) {
                                throw new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Failed to save face image: " + e.getMessage());
                        }
                }

                // 4. Create Student
                Student student = Student.builder()
                                .studentRollNo(dto.getStudentRollNo())
                                .studentName(dto.getStudentName())
                                // .email(dto.getEmail()) // Student entity does not have email
                                .user(user)
                                .classroom(classroom)
                                .faceImagePath(faceImagePath)
                                .createdAt(LocalDateTime.now())
                                .build();

                try {
                        Student savedStudent = studentRepository.save(student);
                        return mapToDTO(savedStudent);
                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                        throw new org.springframework.web.server.ResponseStatusException(
                                        org.springframework.http.HttpStatus.CONFLICT,
                                        "Student with Roll No " + dto.getStudentRollNo() + " already exists.");
                }
        }

        public StudentDTO updateStudentClassroom(Long studentId, Long classroomId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND, "Student not found"));

                Classroom classroom = classroomRepository.findById(classroomId)
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND, "Classroom not found"));

                student.setClassroom(classroom);
                Student saved = studentRepository.save(student);
                return mapToDTO(saved);
        }

        public StudentDTO updateStudent(Long id, StudentDTO dto) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND, "Student not found"));

                if (dto.getStudentName() != null && !dto.getStudentName().isEmpty()) {
                        student.setStudentName(dto.getStudentName());
                }

                if (dto.getClassroomId() != null) {
                        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                                        .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                        org.springframework.http.HttpStatus.NOT_FOUND,
                                                        "Classroom not found with ID: " + dto.getClassroomId()));
                        student.setClassroom(classroom);
                }

                // RollNo update is restricted for now as it affects User identity

                Student saved = studentRepository.save(student);
                return mapToDTO(saved);
        }

        public java.util.List<StudentDTO> getAllStudents() {
                return studentRepository.findAll().stream()
                                .map(this::mapToDTO)
                                .collect(java.util.stream.Collectors.toList());
        }

        public void deleteStudent(Long id) {
                if (!studentRepository.existsById(id)) {
                        throw new org.springframework.web.server.ResponseStatusException(
                                        org.springframework.http.HttpStatus.NOT_FOUND, "Student not found");
                }
                studentRepository.deleteById(id);
        }

        private StudentDTO mapToDTO(Student student) {
                String email = (student.getUser() != null) ? student.getUser().getUsername() : null;
                Long classroomId = (student.getClassroom() != null) ? student.getClassroom().getId() : null;

                return StudentDTO.builder()
                                .id(student.getId())
                                .studentRollNo(student.getStudentRollNo())
                                .studentName(student.getStudentName())
                                .email(email)
                                .classroomId(classroomId)
                                .build();
        }
}
