package com.smartclassroom.backend.service.faculty;

import com.smartclassroom.backend.dto.faculty.*;
import com.smartclassroom.backend.model.*;
import com.smartclassroom.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {

        @Autowired
        private FacultyRepository facultyRepository;

        @Autowired
        private FacultyClassroomRepository facultyClassroomRepository;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private AttendanceRepository attendanceRepository;

        @Autowired
        private ODRequestRepository odRequestRepository;

        private Faculty getFacultyByUsername(String username) {
                return facultyRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Faculty profile not found for user: " + username));
        }

        private void validateClassroomAccess(Faculty faculty, Long classroomId) {
                boolean isAssigned = facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                                .anyMatch(fc -> fc.getClassroom().getId().equals(classroomId));

                if (!isAssigned) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "Access denied: Classroom not assigned to this faculty");
                }
        }

        @Transactional(readOnly = true)
        public List<FacultyClassroomDTO> getAssignedClassrooms(String username) {
                Faculty faculty = getFacultyByUsername(username);
                return facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                                .map(fc -> FacultyClassroomDTO.builder()
                                                .id(fc.getClassroom().getId())
                                                .classroomCode(fc.getClassroom().getClassroomCode())
                                                .classroomName(fc.getClassroom().getClassroomName())
                                                .department(fc.getClassroom().getDepartment())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<FacultyStudentDTO> getStudentsByClassroom(String username, Long classroomId) {
                Faculty faculty = getFacultyByUsername(username);
                validateClassroomAccess(faculty, classroomId);

                return studentRepository.findByClassroomId(classroomId).stream()
                                .map(s -> FacultyStudentDTO.builder()
                                                .id(s.getId())
                                                .studentRollNo(s.getStudentRollNo())
                                                .studentName(s.getStudentName())
                                                .build())
                                .collect(Collectors.toList());
        }

        // Session logic removed.

        @Transactional(readOnly = true)
        public List<FacultyAttendanceDTO> getAttendanceSummary(String username, LocalDate date, Long classroomId) {
                Faculty faculty = getFacultyByUsername(username);
                validateClassroomAccess(faculty, classroomId);

                return attendanceRepository.findByDate(date).stream()
                                .filter(a -> a.getStudent().getClassroom().getId().equals(classroomId))
                                .map(a -> FacultyAttendanceDTO.builder()
                                                .id(a.getId())
                                                .studentRollNo(a.getStudent().getStudentRollNo())
                                                .studentName(a.getStudent().getStudentName())
                                                .status(a.getStatus())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<FacultyODRequestDTO> getODRequests(String username, LocalDate date, Long classroomId) {
                Faculty faculty = getFacultyByUsername(username);
                validateClassroomAccess(faculty, classroomId);

                return odRequestRepository.findByDate(date).stream()
                                .filter(od -> od.getStudent().getClassroom() != null &&
                                                od.getStudent().getClassroom().getId().equals(classroomId))
                                .map(od -> FacultyODRequestDTO.builder()
                                                .id(od.getId())
                                                .studentRollNo(od.getStudent().getStudentRollNo())
                                                .studentName(od.getStudent().getStudentName())
                                                .reason(od.getReason())
                                                .status(od.getStatus())
                                                .requestedAt(od.getRequestedAt())
                                                .build())
                                .collect(Collectors.toList());
        }
}
