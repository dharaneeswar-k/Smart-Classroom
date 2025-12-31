package com.smartclassroom.backend.scheduler;

import com.smartclassroom.backend.service.attendance.DailyAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AttendanceScheduler {

    @Autowired
    private DailyAttendanceService dailyAttendanceService;

    // Run at 4:30 PM every day
    @Scheduled(cron = "0 30 16 * * *")
    public void scheduleDailyAttendance() {
        System.out.println("Running Daily Attendance Calculation...");
        dailyAttendanceService.calculateDailyAttendance(LocalDate.now());
        System.out.println("Daily Attendance Calculation Completed.");
    }
}
