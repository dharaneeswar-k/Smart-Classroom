package com.smartclassroom.backend.dto.faculty;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class FacultySessionDTO {
    private Long id;
    private String subjectName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
