package com.smartclassroom.backend.dto.faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceSummaryDTO {
    private Long studentId;
    private String studentName;
    private String studentRollNo;

    private String session1Status; // PRESENT, ABSENT, UNCERTAIN
    private String session2Status;
    private String session3Status;

    private String dailyStatus; // PRESENT, ABSENT, HALF_DAY, ON_DUTY

    // For specific legacy compatibility if needed, but we prefer explicit fields
    // private Double confidenceScore; // Maybe avg score?
}
