package com.smartclassroom.backend.controller.admin;

import com.smartclassroom.backend.dto.admin.CameraDTO;
import com.smartclassroom.backend.service.admin.AdminCameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cameras")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCameraController {

    @Autowired
    private AdminCameraService cameraService;

    @PostMapping
    public ResponseEntity<CameraDTO> addCamera(@RequestBody CameraDTO dto) {
        return ResponseEntity.ok(cameraService.addCamera(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CameraDTO> updateCamera(@PathVariable Long id, @RequestBody CameraDTO dto) {
        return ResponseEntity.ok(cameraService.updateCamera(id, dto));
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<CameraDTO>> getCamerasByClassroom(@PathVariable Long classroomId) {
        return ResponseEntity.ok(cameraService.getCamerasByClassroom(classroomId));
    }

    @GetMapping
    public ResponseEntity<List<CameraDTO>> getAllCameras() {
        return ResponseEntity.ok(cameraService.getAllCameras());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamera(@PathVariable Long id) {
        cameraService.deleteCamera(id);
        return ResponseEntity.ok().build();
    }
}
