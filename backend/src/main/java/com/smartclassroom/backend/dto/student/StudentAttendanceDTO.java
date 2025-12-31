package com.smartclassroom.backend.dto.student;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class StudentAttendanceDTO {
    // private Long sessionId; // Removed
    private LocalDate date;
    private String classroomName;
    private String status; // PRESENT, ABSENT, OD
    private Double attendancePercentage;
    private Boolean lateEntry;
    private Boolean earlyExit;
}
