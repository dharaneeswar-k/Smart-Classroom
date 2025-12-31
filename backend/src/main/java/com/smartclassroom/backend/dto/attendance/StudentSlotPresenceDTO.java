package com.smartclassroom.backend.dto.attendance;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class StudentSlotPresenceDTO {
    private Long studentId;
    private Long sessionId;
    private Set<Integer> presentSlotIndexes;
}
