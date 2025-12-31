package com.smartclassroom.backend.controller.faculty;

import com.smartclassroom.backend.dto.faculty.SessionSummaryDTO;
import com.smartclassroom.backend.service.faculty.FacultySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/faculty/classrooms")
@PreAuthorize("hasRole('FACULTY')")
public class FacultySessionController {

    @Autowired
    private FacultySessionService facultySessionService;

    @GetMapping("/{classroomId}/sessions")
    public ResponseEntity<List<SessionSummaryDTO>> getSessions(
            @PathVariable Long classroomId,
            Principal principal) {
        return ResponseEntity.ok(facultySessionService.getSessions(principal.getName(), classroomId));
    }
}
