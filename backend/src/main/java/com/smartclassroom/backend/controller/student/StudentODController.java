package com.smartclassroom.backend.controller.student;

import com.smartclassroom.backend.dto.student.StudentODHistoryDTO;
import com.smartclassroom.backend.dto.student.StudentODRequestDTO;
import com.smartclassroom.backend.service.student.StudentODService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student/od")
@PreAuthorize("hasRole('STUDENT')")
public class StudentODController {

    @Autowired
    private StudentODService studentODService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyForOD(@RequestBody StudentODRequestDTO requestDTO, Principal principal) {
        studentODService.applyForOD(principal.getName(), requestDTO);
        return ResponseEntity.ok("OD Request submitted successfully");
    }

    @GetMapping("/history")
    public ResponseEntity<List<StudentODHistoryDTO>> getODHistory(Principal principal) {
        return ResponseEntity.ok(studentODService.getODHistory(principal.getName()));
    }
}
