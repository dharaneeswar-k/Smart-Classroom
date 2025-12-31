package com.smartclassroom.backend.controller.student;

import com.smartclassroom.backend.dto.student.StudentAttendancePercentageDTO;
import com.smartclassroom.backend.service.student.StudentAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/student/attendance/percentage")
@PreAuthorize("hasRole('STUDENT')")
public class StudentAnalyticsController {

    @Autowired
    private StudentAnalyticsService studentAnalyticsService;

    @GetMapping
    public ResponseEntity<StudentAttendancePercentageDTO> getAttendancePercentage(Principal principal) {
        return ResponseEntity.ok(studentAnalyticsService.getAttendancePercentage(principal.getName()));
    }
}
