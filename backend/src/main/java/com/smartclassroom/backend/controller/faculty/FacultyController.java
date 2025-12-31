package com.smartclassroom.backend.controller.faculty;

import com.smartclassroom.backend.dto.faculty.*;
import com.smartclassroom.backend.model.Session;
import com.smartclassroom.backend.repository.SessionRepository;
import com.smartclassroom.backend.service.faculty.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/faculty")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/assignments")
    public ResponseEntity<List<FacultyClassroomDTO>> getAssignedClassrooms(Principal principal) {
        return ResponseEntity.ok(facultyService.getAssignedClassrooms(principal.getName()));
    }

    @GetMapping("/classroom/{classroomId}/students")
    public ResponseEntity<List<FacultyStudentDTO>> getStudentsByClassroom(
            @PathVariable Long classroomId,
            Principal principal) {
        return ResponseEntity.ok(facultyService.getStudentsByClassroom(principal.getName(), classroomId));
    }

    @GetMapping("/classroom/{classroomId}/sessions")
    public ResponseEntity<List<FacultySessionDTO>> getSessions(
            @PathVariable Long classroomId,
            Principal principal) {
        // Direct repository fetch to bridge gap
        return ResponseEntity.ok(sessionRepository.findByClassroomId(classroomId).stream()
                .map(s -> FacultySessionDTO.builder()
                        .id(s.getId())
                        .subjectName(s.getSubjectName())
                        .sessionDate(s.getSessionDate())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .status(s.getStatus())
                        .build())
                .collect(Collectors.toList()));
    }

    @GetMapping("/session/{sessionId}/attendance")
    public ResponseEntity<List<FacultyAttendanceDTO>> getAttendanceSummary(
            @PathVariable Long sessionId,
            Principal principal) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        return ResponseEntity.ok(facultyService.getAttendanceSummary(principal.getName(), session.getSessionDate(),
                session.getClassroom().getId()));
    }

    @GetMapping("/session/{sessionId}/od-requests")
    public ResponseEntity<List<FacultyODRequestDTO>> getODRequests(
            @PathVariable Long sessionId,
            Principal principal) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        return ResponseEntity.ok(facultyService.getODRequests(principal.getName(), session.getSessionDate(),
                session.getClassroom().getId()));
    }
}
