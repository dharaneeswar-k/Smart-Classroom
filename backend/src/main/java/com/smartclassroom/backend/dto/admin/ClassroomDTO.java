package com.smartclassroom.backend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomDTO {
    private Long id;
    private String classroomCode;
    private String classroomName;
    private String department;
}
