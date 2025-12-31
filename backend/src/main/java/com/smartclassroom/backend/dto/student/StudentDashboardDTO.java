package com.smartclassroom.backend.dto.student;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDashboardDTO {
    private long totalDays;
    private double daysPresent; // Changed to double for Half Days
    private double attendancePercentage;
    private long totalODRequests;
    private long approvedODRequests;
    private long pendingODRequests;
}
