package com.smartclassroom.backend.dto.attendance;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PresenceEventIngestDTO {
    private Long studentId;
    private Long cameraId;
    // Session ID removed as per new requirement
    private LocalDateTime timestamp;
    private Double confidenceScore;
}
