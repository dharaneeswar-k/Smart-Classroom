package com.smartclassroom.backend.dto.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentODRequestDTO {
    // private Long sessionId;
    private java.time.LocalDate date;
    private String reason;
}
