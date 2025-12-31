package com.smartclassroom.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiStudentDTO {
    private String studentId; // Roll No
    private String classroomId; // Classroom Code
}
