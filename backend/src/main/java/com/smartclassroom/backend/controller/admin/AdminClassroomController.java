package com.smartclassroom.backend.controller.admin;

import com.smartclassroom.backend.dto.admin.ClassroomDTO;
import com.smartclassroom.backend.service.admin.AdminClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/classrooms")
@PreAuthorize("hasRole('ADMIN')")
public class AdminClassroomController {

    @Autowired
    private AdminClassroomService classroomService;

    @PostMapping
    public ResponseEntity<ClassroomDTO> createClassroom(@RequestBody ClassroomDTO dto) {
        return ResponseEntity.ok(classroomService.createClassroom(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClassroomDTO>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomDTO> updateClassroom(@PathVariable Long id, @RequestBody ClassroomDTO dto) {
        return ResponseEntity.ok(classroomService.updateClassroom(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }
}
