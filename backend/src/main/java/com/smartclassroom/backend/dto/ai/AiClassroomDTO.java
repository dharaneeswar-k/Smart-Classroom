package com.smartclassroom.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiClassroomDTO {
    private String classroomId; // Classroom Code
    private String cameraId; // Camera Code
}
