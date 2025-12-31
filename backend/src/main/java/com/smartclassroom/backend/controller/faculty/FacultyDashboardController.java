package com.smartclassroom.backend.controller.faculty;

import com.smartclassroom.backend.dto.faculty.ClassroomSummaryDTO;
import com.smartclassroom.backend.service.faculty.FacultyDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/faculty/dashboard")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyDashboardController {

    @Autowired
    private FacultyDashboardService facultyDashboardService;

    @GetMapping("/classrooms")
    public ResponseEntity<List<ClassroomSummaryDTO>> getAssignedClassrooms(Principal principal) {
        return ResponseEntity.ok(facultyDashboardService.getAssignedClassrooms(principal.getName()));
    }
}
