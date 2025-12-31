package com.smartclassroom.backend.controller.student;

import com.smartclassroom.backend.dto.student.StudentAttendanceDTO;
import com.smartclassroom.backend.service.student.StudentAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student/attendance")
@PreAuthorize("hasRole('STUDENT')")
public class StudentAttendanceController {

    @Autowired
    private StudentAttendanceService studentAttendanceService;

    @GetMapping
    public ResponseEntity<List<StudentAttendanceDTO>> getAttendanceHistory(Principal principal) {
        return ResponseEntity.ok(studentAttendanceService.getAttendanceHistory(principal.getName()));
    }
}
