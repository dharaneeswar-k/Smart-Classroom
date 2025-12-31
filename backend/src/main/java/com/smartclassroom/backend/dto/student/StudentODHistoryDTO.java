package com.smartclassroom.backend.dto.student;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class StudentODHistoryDTO {
    private Long id;
    private String subjectName;
    // private java.time.LocalDateTime sessionDate;
    private java.time.LocalDate date;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime requestedAt;
    private LocalDateTime decisionAt;
}
