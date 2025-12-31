package com.smartclassroom.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveCameraDTO {
    private String cameraCode;
    private String cameraHttpUrl;
    private String classroomCode;
}
