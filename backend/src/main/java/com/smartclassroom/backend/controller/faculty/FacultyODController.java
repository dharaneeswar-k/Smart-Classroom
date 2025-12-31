package com.smartclassroom.backend.controller.faculty;

import com.smartclassroom.backend.dto.faculty.ODRequestSummaryDTO;
import com.smartclassroom.backend.service.faculty.FacultyODService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/faculty/sessions")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyODController {

    @Autowired
    private FacultyODService facultyODService;

    @Autowired
    private com.smartclassroom.backend.repository.SessionRepository sessionRepository;

    @GetMapping("/{sessionId}/od-requests")
    public ResponseEntity<List<ODRequestSummaryDTO>> getODRequests(
            @PathVariable Long sessionId,
            Principal principal) {
        com.smartclassroom.backend.model.Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Session not found"));
        return ResponseEntity.ok(facultyODService.getODRequests(principal.getName(), session.getSessionDate(),
                session.getClassroom().getId()));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ODRequestSummaryDTO>> getPendingODRequests(Principal principal) {
        return ResponseEntity.ok(facultyODService.getPendingODRequests(principal.getName()));
    }

    @PostMapping("/{odId}/approve")
    public ResponseEntity<Void> approveOD(@PathVariable Long odId, Principal principal) {
        facultyODService.approveOD(principal.getName(), odId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{odId}/reject")
    public ResponseEntity<Void> rejectOD(@PathVariable Long odId, Principal principal) {
        facultyODService.rejectOD(principal.getName(), odId);
        return ResponseEntity.ok().build();
    }
}
