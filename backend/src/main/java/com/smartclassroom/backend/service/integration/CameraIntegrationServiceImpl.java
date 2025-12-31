package com.smartclassroom.backend.service.integration;

import com.smartclassroom.backend.dto.ActiveCameraDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CameraIntegrationServiceImpl implements CameraIntegrationService {

    @Override
    public List<ActiveCameraDTO> getActiveCameras() {
        return Collections.emptyList();
    }
}
