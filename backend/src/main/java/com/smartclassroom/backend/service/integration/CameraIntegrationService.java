package com.smartclassroom.backend.service.integration;

import com.smartclassroom.backend.dto.ActiveCameraDTO;
import java.util.List;

public interface CameraIntegrationService {
    List<ActiveCameraDTO> getActiveCameras();
}
