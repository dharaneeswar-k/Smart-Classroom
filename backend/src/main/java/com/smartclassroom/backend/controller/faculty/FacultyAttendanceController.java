package com.smartclassroom.backend.controller.faculty;

import com.smartclassroom.backend.dto.faculty.AttendanceSummaryDTO;
import com.smartclassroom.backend.service.faculty.FacultyAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/faculty/attendance")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyAttendanceController {

    @Autowired
    private FacultyAttendanceService facultyAttendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceSummaryDTO>> getAttendanceByClassAndDate(
            @RequestParam Long classroomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal) {
        return ResponseEntity
                .ok(facultyAttendanceService.getAttendanceSummaryByClass(principal.getName(), classroomId, date));
    }

    @PostMapping("/resolve")
    public ResponseEntity<Void> resolveSession(
            @RequestBody Map<String, Object> payload,
            Principal principal) {

        Long studentId = Long.valueOf(payload.get("studentId").toString());
        LocalDate date = LocalDate.parse(payload.get("date").toString());
        Integer sessionNum = Integer.valueOf(payload.get("sessionNumber").toString());
        String status = payload.get("status").toString();

        facultyAttendanceService.resolveSession(principal.getName(), studentId, date, sessionNum, status);
        return ResponseEntity.ok().build();
    }
}
