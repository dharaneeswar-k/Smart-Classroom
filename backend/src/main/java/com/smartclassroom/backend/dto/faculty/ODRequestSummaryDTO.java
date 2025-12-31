package com.smartclassroom.backend.dto.faculty;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ODRequestSummaryDTO {
    private Long id;
    private String studentRollNo;
    private String studentName;
    private String reason;
    private String status;
    private LocalDateTime requestedAt;
}
