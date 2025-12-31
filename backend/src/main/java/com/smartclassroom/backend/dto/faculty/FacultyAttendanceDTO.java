package com.smartclassroom.backend.dto.faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyAttendanceDTO {
    private Long id;
    private String studentRollNo;
    private String studentName;
    private String status;
}
