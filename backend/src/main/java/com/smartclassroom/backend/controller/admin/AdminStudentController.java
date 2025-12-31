package com.smartclassroom.backend.controller.admin;

import com.smartclassroom.backend.dto.admin.StudentDTO;
import com.smartclassroom.backend.service.admin.AdminStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/students")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStudentController {

    @Autowired
    private AdminStudentService studentService;

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentDTO> createStudent(
            @RequestPart("student") String studentDTOJson,
            @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image)
            throws com.fasterxml.jackson.core.JsonProcessingException {
        System.out.println("DEBUG: createStudent called");
        System.out.println("DEBUG: studentDTOJson = " + studentDTOJson);
        if (image != null) {
            System.out.println("DEBUG: image name = " + image.getOriginalFilename());
        } else {
            System.out.println("DEBUG: image is null");
        }

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            StudentDTO dto = mapper.readValue(studentDTOJson, StudentDTO.class);
            return ResponseEntity.ok(studentService.createStudent(dto, image));
        } catch (Exception e) {
            System.out.println("DEBUG: Exception in createStudent: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{studentId}/classroom/{classroomId}")
    public ResponseEntity<StudentDTO> updateStudentClassroom(
            @PathVariable Long studentId,
            @PathVariable Long classroomId) {
        return ResponseEntity.ok(studentService.updateStudentClassroom(studentId, classroomId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @GetMapping
    public ResponseEntity<java.util.List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
