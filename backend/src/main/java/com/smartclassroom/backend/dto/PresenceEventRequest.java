package com.smartclassroom.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresenceEventRequest {
    private String studentId;
    private Long cameraId;
    private String timestamp;
    private Double confidenceScore;
}
