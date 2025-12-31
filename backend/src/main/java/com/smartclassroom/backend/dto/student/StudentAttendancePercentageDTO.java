package com.smartclassroom.backend.dto.student;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentAttendancePercentageDTO {
    private Double attendancePercentage;
}
