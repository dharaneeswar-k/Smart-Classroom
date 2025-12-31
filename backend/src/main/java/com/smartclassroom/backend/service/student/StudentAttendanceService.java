package com.smartclassroom.backend.service.student;

import com.smartclassroom.backend.dto.student.StudentAttendanceDTO;
import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.repository.AttendanceRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentAttendanceService {

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private AttendanceRepository attendanceRepository;

        @Transactional(readOnly = true)
        public List<StudentAttendanceDTO> getAttendanceHistory(String username) {
                Student student = studentRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Student profile not found"));

                return attendanceRepository.findByStudentId(student.getId()).stream()
                                .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate())) // Sort by most recent
                                .map(a -> StudentAttendanceDTO.builder()
                                                .date(a.getDate())
                                                .classroomName(student.getClassroom().getClassroomName())
                                                .status(a.getStatus())
                                                .attendancePercentage(a.getAttendancePercentage())
                                                .lateEntry(a.getLateEntryFlag())
                                                .earlyExit(a.getEarlyExitFlag())
                                                .build())
                                .collect(Collectors.toList());
        }
}
