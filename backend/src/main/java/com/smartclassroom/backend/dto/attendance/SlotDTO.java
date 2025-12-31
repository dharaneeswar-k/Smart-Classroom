package com.smartclassroom.backend.dto.attendance;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SlotDTO {
    private int slotIndex;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
