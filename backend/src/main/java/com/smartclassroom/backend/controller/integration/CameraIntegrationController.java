package com.smartclassroom.backend.controller.integration;

import com.smartclassroom.backend.dto.ActiveCameraDTO;
import com.smartclassroom.backend.service.integration.CameraIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
@RequiredArgsConstructor
public class CameraIntegrationController {

    private final CameraIntegrationService cameraIntegrationService;

    @GetMapping("/active")
    public ResponseEntity<List<ActiveCameraDTO>> getActiveCameras() {
        return ResponseEntity.ok(cameraIntegrationService.getActiveCameras());
    }
}
