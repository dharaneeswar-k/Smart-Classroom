package com.smartclassroom.backend.service.attendance;

import com.smartclassroom.backend.model.*;
import com.smartclassroom.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyAttendanceService {

    private final StudentRepository studentRepository;
    private final PresenceEventRepository presenceEventRepository;
    private final AttendanceRepository attendanceRepository;
    private final SessionAttendanceRepository sessionAttendanceRepository;
    private final ODRequestRepository odRequestRepository;
    private final FacultyRepository facultyRepository;

    // Time Slots Configuration
    private static final LocalTime SLOT1_START = LocalTime.of(9, 15);
    private static final LocalTime SLOT1_END = LocalTime.of(11, 00);

    private static final LocalTime SLOT2_START = LocalTime.of(11, 25);
    private static final LocalTime SLOT2_END = LocalTime.of(13, 00);

    private static final LocalTime SLOT3_START = LocalTime.of(14, 00);
    private static final LocalTime SLOT3_END = LocalTime.of(16, 30);

    // Rule Constants
    private static final int WINDOW_SIZE_MINUTES = 5;
    private static final double MIN_COVERAGE_PERCENTAGE = 0.30;
    private static final double CLASS_CONTEXT_THRESHOLD = 0.70;

    @Transactional
    public void calculateDailyAttendance(LocalDate date) {
        log.info("Starting Daily Attendance Calculation for date: {}", date);
        List<Student> students = studentRepository.findAll();

        // Pre-calculate Class Context (Rule 4) Per Session
        // This is a simplified per-classroom calculation
        // Returns true if class context is "Active" (>= 70% presence)
        // We will compute this on the fly or pre-compute.
        // For simplicity in this loop, we might need a preliminary pass or handle it
        // student-by-student with cached helpers.
        // Let's do a preliminary pass to get "Raw Presence" counts per classroom per
        // session to establish Context.

        for (Student student : students) {
            try {
                processStudentAttendance(student, date);
            } catch (Exception e) {
                log.error("Error processing attendance for student {}: {}", student.getId(), e.getMessage());
            }
        }
        log.info("Daily Attendance Calculation Completed.");
    }

    private void processStudentAttendance(Student student, LocalDate date) {
        // Step 0: OD Authority Rule
        // Check if Approved OD exists
        boolean hasApprovedOD = odRequestRepository.findByDate(date).stream()
                .anyMatch(od -> od.getStudent().getId().equals(student.getId()) && "APPROVED".equals(od.getStatus()));

        if (hasApprovedOD) {
            saveDailyAttendance(student, date, "ON_DUTY", 100.0);
            log.info("Student {} marked ON_DUTY (OD Override)", student.getStudentRollNo());
            return;
        }

        // Check if Daily Attendance already exists (and is not ON_DUTY which we just
        // overwrote if needed, or maybe we want to re-calc)
        if (attendanceRepository.existsByStudentIdAndDate(student.getId(), date)) {
            // Check if we need to update? For now, let's assume if it exists, we skip
            // unless forced.
            // But we might want to re-run if it was UNCERTAIN before.
            // Let's assume we proceed to check Session inputs.
        }

        // Fetch Raw Events
        List<PresenceEvent> allEvents = presenceEventRepository.findByStudentIdAndDate(student.getId(), date);

        // Calculate Session Statuses
        String s1Status = evaluateSession(student, date, 1, allEvents, SLOT1_START, SLOT1_END);
        String s2Status = evaluateSession(student, date, 2, allEvents, SLOT2_START, SLOT2_END);
        String s3Status = evaluateSession(student, date, 3, allEvents, SLOT3_START, SLOT3_END);

        // Step 3: Persist Session Statuses
        saveSessionAttendance(student, date, 1, s1Status);
        saveSessionAttendance(student, date, 2, s2Status);
        saveSessionAttendance(student, date, 3, s3Status);

        // Step 4: Aggregate to Daily (Blocking Rule)
        if ("UNCERTAIN".equals(s1Status) || "UNCERTAIN".equals(s2Status) || "UNCERTAIN".equals(s3Status)) {
            log.info("Student {} has UNCERTAIN sessions. Daily Attendance BLOCKED.", student.getStudentRollNo());
            return; // STOP. Do not save Daily Attendance.
        }

        // Aggregate
        int presentCount = 0;
        if ("PRESENT".equals(s1Status))
            presentCount++;
        if ("PRESENT".equals(s2Status))
            presentCount++;
        if ("PRESENT".equals(s3Status))
            presentCount++;

        String finalStatus = "ABSENT";
        if (presentCount >= 2) { // 3 or 2
            finalStatus = "PRESENT";
        } else if (presentCount == 1) {
            finalStatus = "HALF_DAY";
        }

        double percentage = (presentCount / 3.0) * 100.0;
        saveDailyAttendance(student, date, finalStatus, percentage);
    }

    private String evaluateSession(Student student, LocalDate date, int sessionNum, List<PresenceEvent> allEvents,
            LocalTime start, LocalTime end) {
        // Filter events for this session
        List<PresenceEvent> sessionEvents = allEvents.stream()
                .filter(e -> !e.getEventTime().toLocalTime().isBefore(start)
                        && !e.getEventTime().toLocalTime().isAfter(end))
                .collect(Collectors.toList());

        // Rule 1: Window Sampling
        long totalMinutes = ChronoUnit.MINUTES.between(start, end);
        int totalWindows = (int) Math.ceil((double) totalMinutes / WINDOW_SIZE_MINUTES);

        Set<Integer> presentWindows = new HashSet<>();
        for (PresenceEvent event : sessionEvents) {
            long minutesFromStart = ChronoUnit.MINUTES.between(start, event.getEventTime().toLocalTime());
            int windowIndex = (int) (minutesFromStart / WINDOW_SIZE_MINUTES);
            if (windowIndex >= 0 && windowIndex < totalWindows) {
                presentWindows.add(windowIndex);
            }
        }

        // Rule 2: Evidence Accumulation
        int presenceCount = presentWindows.size();

        // Rule 3: Minimum Coverage
        double coverage = (double) presenceCount / totalWindows;
        boolean meetsThreshold = coverage >= MIN_COVERAGE_PERCENTAGE;

        if (meetsThreshold) {
            return "PRESENT";
        }

        // Rule 5: Zero Evidence Absence (Check BEFORE Context to be safe/efficient,
        // though context theoretically captures this)
        if (presenceCount == 0) {
            return "ABSENT";
        }

        // Rule 4: Classroom Context Validation
        if (isClassroomContextActive(student.getClassroom(), date, start, end)) {
            // If class is active (>70% present), and student has ANY evidence (>0), we
            // marked them PRESENT
            // But wait, user said "student did NOT meet minimum coverage" AND
            // "PresenceCount > 0"
            // We already know coverage < min (otherwise we returned PRESENT).
            // We known count > 0 (otherwise we returned ABSENT).
            return "PRESENT";
        }

        // Rule 6: Fallback to UNCERTAIN
        return "UNCERTAIN";
    }

    private boolean isClassroomContextActive(Classroom classroom, LocalDate date, LocalTime start, LocalTime end) {
        if (classroom == null)
            return false;

        // This is expensive to calculate properly on the fly.
        // Ideally, we'd cache this or calculate it once per session per class.
        // For this implementation, I will implement a simplified check:
        // Count how many students in this class have > MIN_COVERAGE_PERCENTAGE for this
        // session.

        List<Student> classStudents = studentRepository.findAll().stream() // Ideally findByClassroom
                .filter(s -> s.getClassroom() != null && s.getClassroom().getId().equals(classroom.getId()))
                .collect(Collectors.toList());

        if (classStudents.isEmpty())
            return false;

        int presentStudents = 0;
        for (Student s : classStudents) {
            List<PresenceEvent> sEvents = presenceEventRepository.findByStudentIdAndDate(s.getId(), date);
            // ... duplicate window logic?
            // To avoid infinite recursion, we just check Raw Count as a proxy for "Active
            // Class" to save perf?
            // Or we strictly implement the logic. Let's do a quick raw check for perf.
            // "Is the number of students with detections > 70%?"

            long distinctWindows = sEvents.stream()
                    .filter(e -> !e.getEventTime().toLocalTime().isBefore(start)
                            && !e.getEventTime().toLocalTime().isAfter(end))
                    .map(e -> ChronoUnit.MINUTES.between(start, e.getEventTime().toLocalTime()) / WINDOW_SIZE_MINUTES)
                    .distinct()
                    .count();

            long totalMinutes = ChronoUnit.MINUTES.between(start, end);
            int totalWindows = (int) Math.ceil((double) totalMinutes / WINDOW_SIZE_MINUTES);
            if ((double) distinctWindows / totalWindows >= MIN_COVERAGE_PERCENTAGE) {
                presentStudents++;
            }
        }

        return ((double) presentStudents / classStudents.size()) >= CLASS_CONTEXT_THRESHOLD;
    }

    private void saveSessionAttendance(Student student, LocalDate date, int sessionNum, String status) {
        SessionAttendance record = sessionAttendanceRepository
                .findByStudentIdAndDateAndSessionNumber(student.getId(), date, sessionNum)
                .orElse(SessionAttendance.builder()
                        .student(student)
                        .date(date)
                        .sessionNumber(sessionNum)
                        .build());

        // Only update if not already resolved manually?
        // If resolvedBy is set, we generally shouldn't overwrite unless we want to
        // reset?
        // User didn't specify reset policy. Assuming Recalculation overrides UNLESS
        // resolved.
        if (record.getResolvedBy() != null) {
            log.info("Skipping overwrite of resolved session {} for student {}", sessionNum,
                    student.getStudentRollNo());
            return;
        }

        record.setStatus(status);
        sessionAttendanceRepository.save(record);
    }

    private void saveDailyAttendance(Student student, LocalDate date, String status, double percentage) {
        Attendance attendance = attendanceRepository.findByStudentIdAndDate(student.getId(), date) // we need to add
                                                                                                   // this method to
                                                                                                   // repo if not
                                                                                                   // exists, or handle
                                                                                                   // uniqueness
                .orElse(Attendance.builder().student(student).date(date).build());

        attendance.setStatus(status);
        attendance.setAttendancePercentage(percentage);
        attendance.setGeneratedAt(LocalDateTime.now());
        attendance.setLateEntryFlag(false); // To be implemented if needed
        attendance.setEarlyExitFlag(false);

        attendanceRepository.save(attendance);
    }

    // Resolution Method
    @Transactional
    public void resolveSessionStatus(Long studentId, LocalDate date, int sessionNum, String newStatus,
            String facultyUsername) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        Faculty faculty = facultyRepository.findByUserUsername(facultyUsername).orElseThrow();

        SessionAttendance record = sessionAttendanceRepository
                .findByStudentIdAndDateAndSessionNumber(studentId, date, sessionNum)
                .orElse(SessionAttendance.builder()
                        .student(student)
                        .date(date)
                        .sessionNumber(sessionNum)
                        .build());

        record.setStatus(newStatus);
        record.setResolvedBy(faculty);
        record.setResolvedAt(LocalDateTime.now());
        sessionAttendanceRepository.save(record);

        // Trigger Re-Aggregation
        processStudentAttendance(student, date);
    }
}
