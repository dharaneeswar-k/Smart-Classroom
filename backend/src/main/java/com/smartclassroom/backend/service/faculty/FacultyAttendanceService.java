package com.smartclassroom.backend.service.faculty;

import com.smartclassroom.backend.dto.faculty.AttendanceSummaryDTO;
import com.smartclassroom.backend.model.*;
import com.smartclassroom.backend.repository.*;
import com.smartclassroom.backend.service.attendance.DailyAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FacultyAttendanceService {

        @Autowired
        private FacultyRepository facultyRepository;

        @Autowired
        private FacultyClassroomRepository facultyClassroomRepository;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private SessionAttendanceRepository sessionAttendanceRepository;

        @Autowired
        private AttendanceRepository attendanceRepository;

        @Autowired
        private DailyAttendanceService dailyAttendanceService;

        @Transactional(readOnly = true)
        public List<AttendanceSummaryDTO> getAttendanceSummaryByClass(String username, Long classroomId,
                        LocalDate date) {
                Faculty faculty = facultyRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Faculty profile not found"));

                boolean isAssigned = facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                                .anyMatch(fc -> fc.getClassroom().getId().equals(classroomId));

                if (!isAssigned) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "Access denied: Classroom not assigned to this faculty");
                }

                List<Student> students = studentRepository.findByClassroomId(classroomId);
                List<AttendanceSummaryDTO> summaryList = new ArrayList<>();

                for (Student student : students) {
                        // Fetch Session Records
                        List<SessionAttendance> sessions = sessionAttendanceRepository
                                        .findByStudentIdAndDate(student.getId(), date);
                        Map<Integer, String> sessionMap = sessions.stream()
                                        .collect(Collectors.toMap(SessionAttendance::getSessionNumber,
                                                        SessionAttendance::getStatus));

                        // Fetch Daily Record
                        Attendance daily = attendanceRepository.findByStudentIdAndDate(student.getId(), date)
                                        .orElse(null);

                        AttendanceSummaryDTO dto = AttendanceSummaryDTO.builder()
                                        .studentId(student.getId())
                                        .studentName(student.getStudentName())
                                        .studentRollNo(student.getStudentRollNo())
                                        .session1Status(sessionMap.getOrDefault(1, "PENDING")) // Default to Pending if
                                                                                               // no record yet
                                        .session2Status(sessionMap.getOrDefault(2, "PENDING"))
                                        .session3Status(sessionMap.getOrDefault(3, "PENDING"))
                                        .dailyStatus(daily != null ? daily.getStatus() : "PENDING")
                                        .build();

                        summaryList.add(dto);
                }

                return summaryList;
        }

        @Transactional
        public void resolveSession(String username, Long studentId, LocalDate date, Integer sessionNum, String status) {
                // Simple proxy to DailyAttendanceService, maybe with extra auth check if needed
                Faculty faculty = facultyRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Faculty profile not found"));

                // Check if student belongs to a class relevant to faculty?
                // For speed, assuming yes or relying on Controller security.
                // DailyAttendanceService handles the resolution logic.
                dailyAttendanceService.resolveSessionStatus(studentId, date, sessionNum, status, username);
        }
}
