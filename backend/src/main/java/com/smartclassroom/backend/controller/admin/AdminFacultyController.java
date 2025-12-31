package com.smartclassroom.backend.controller.admin;

import com.smartclassroom.backend.dto.admin.FacultyDTO;
import com.smartclassroom.backend.service.admin.AdminFacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/faculty")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFacultyController {

    @Autowired
    private AdminFacultyService facultyService;

    @PostMapping
    public ResponseEntity<FacultyDTO> createFaculty(@RequestBody FacultyDTO dto) {
        return ResponseEntity.ok(facultyService.createFaculty(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyDTO> updateFaculty(@PathVariable Long id, @RequestBody FacultyDTO dto) {
        return ResponseEntity.ok(facultyService.updateFaculty(id, dto));
    }

    @PostMapping("/{facultyId}/assign")
    public ResponseEntity<Void> assignFacultyToClassrooms(
            @PathVariable Long facultyId,
            @RequestBody List<Long> classroomIds) {
        facultyService.assignFacultyToClassrooms(facultyId, classroomIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FacultyDTO>> getAllFaculty() {
        return ResponseEntity.ok(facultyService.getAllFaculty());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
}
