package com.smartclassroom.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiPresenceEventDTO {
    private String studentId;
    private String cameraId;
    private String timestamp;
    private Double confidenceScore;
}
