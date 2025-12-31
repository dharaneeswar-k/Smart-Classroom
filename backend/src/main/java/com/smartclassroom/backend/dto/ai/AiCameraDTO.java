package com.smartclassroom.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiCameraDTO {
    private String cameraId; // Camera Code
    private String streamUrl; // Camera HTTP URL
}
