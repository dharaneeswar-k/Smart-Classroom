package com.smartclassroom.backend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraDTO {
    private Long id;
    private String cameraCode;
    private String cameraHttpUrl;
    private Long classroomId;
    private String status;
}
