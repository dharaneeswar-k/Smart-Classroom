package com.smartclassroom.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiBootstrapDTO {
    private List<AiStudentDTO> students;
    private List<AiClassroomDTO> classrooms;
    private List<AiCameraDTO> cameras;
}
