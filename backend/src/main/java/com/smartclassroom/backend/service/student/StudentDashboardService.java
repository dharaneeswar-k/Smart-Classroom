package com.smartclassroom.backend.service.student;

import com.smartclassroom.backend.dto.student.StudentDashboardDTO;
import com.smartclassroom.backend.model.Attendance;
import com.smartclassroom.backend.model.ODRequest;
import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.repository.AttendanceRepository;
import com.smartclassroom.backend.repository.ODRequestRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentDashboardService {

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private AttendanceRepository attendanceRepository;

        @Autowired
        private ODRequestRepository odRequestRepository;

        @Transactional(readOnly = true)
        public StudentDashboardDTO getDashboardStats(String username) {
                Student student = studentRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Student profile not found"));

                List<Attendance> attendanceRecords = attendanceRepository.findByStudentId(student.getId());
                List<ODRequest> odRequests = odRequestRepository.findByStudentId(student.getId());

                long totalDays = attendanceRecords.size();

                double daysPresent = attendanceRecords.stream()
                                .mapToDouble(a -> {
                                        if ("PRESENT".equals(a.getStatus()) || "OD".equals(a.getStatus()))
                                                return 1.0;
                                        if ("HALF_DAY".equals(a.getStatus()))
                                                return 0.5;
                                        return 0.0;
                                })
                                .sum();

                double percentage = totalDays == 0 ? 0.0 : (daysPresent / totalDays) * 100.0;

                long totalOD = odRequests.size();
                long approvedOD = odRequests.stream().filter(od -> "APPROVED".equals(od.getStatus())).count();
                long pendingOD = odRequests.stream().filter(od -> "PENDING".equals(od.getStatus())).count();

                return StudentDashboardDTO.builder()
                                .totalDays(totalDays)
                                .daysPresent(daysPresent)
                                .attendancePercentage(percentage)
                                .totalODRequests(totalOD)
                                .approvedODRequests(approvedOD)
                                .pendingODRequests(pendingOD)
                                .build();
        }
}
