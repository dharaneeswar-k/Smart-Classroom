package com.smartclassroom.backend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDTO {
    private Long id;
    private String employeeId; // username
    private String facultyName;
    private String designation;
    private String email;
    private String password;
    private List<Long> classroomIds;
}
