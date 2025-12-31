package com.smartclassroom.backend.controller.student;

import com.smartclassroom.backend.dto.student.StudentDashboardDTO;
import com.smartclassroom.backend.service.student.StudentDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/student/dashboard")
@PreAuthorize("hasRole('STUDENT')")
public class StudentDashboardController {

    @Autowired
    private StudentDashboardService studentDashboardService;

    @GetMapping
    public ResponseEntity<StudentDashboardDTO> getDashboardStats(Principal principal) {
        return ResponseEntity.ok(studentDashboardService.getDashboardStats(principal.getName()));
    }
}
