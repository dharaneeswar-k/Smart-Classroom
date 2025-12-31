package com.smartclassroom.backend.dto.faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyClassroomDTO {
    private Long id;
    private String classroomCode;
    private String classroomName;
    private String department;
}
