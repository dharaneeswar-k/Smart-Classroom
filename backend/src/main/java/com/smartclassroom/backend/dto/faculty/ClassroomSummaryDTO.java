package com.smartclassroom.backend.dto.faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomSummaryDTO {
    private Long id;
    private String classroomCode;
    private String classroomName;
    // Removed department to match "id, code, name" requirement strictly?
    // Requirement says "Returns classroom id, code, name".
    // I will include department if helpful but strictly speaking "id, code, name"
    // was listed.
    // However, existing DTO had it. I'll keep it as it's useful context.
    // Wait, strictly "Reads FacultyClassroom mapping [...] Returns classroom id,
    // code, name".
    // I'll stick to a minimal set but keep department as it's usually needed.
    // Actually, I'll follow the previous DTO structure but rename it.
    private String department;
}
