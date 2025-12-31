package com.smartclassroom.backend.service.student;

import com.smartclassroom.backend.dto.student.StudentAttendancePercentageDTO;
import com.smartclassroom.backend.model.Attendance;
import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.repository.AttendanceRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentAnalyticsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Transactional(readOnly = true)
    public StudentAttendancePercentageDTO getAttendancePercentage(String username) {
        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student profile not found"));

        List<Attendance> attendanceRecords = attendanceRepository.findByStudentId(student.getId());

        long totalSessions = attendanceRecords.size();
        long attendedSessions = attendanceRecords.stream()
                .filter(a -> "PRESENT".equals(a.getStatus()) || "OD".equals(a.getStatus()))
                .count();

        double percentage = totalSessions == 0 ? 0.0 : ((double) attendedSessions / totalSessions) * 100.0;

        return StudentAttendancePercentageDTO.builder()
                .attendancePercentage(percentage)
                .build();
    }
}
