package com.smartclassroom.backend.service.student;

import com.smartclassroom.backend.dto.student.StudentODHistoryDTO;
import com.smartclassroom.backend.dto.student.StudentODRequestDTO;
import com.smartclassroom.backend.model.ODRequest;
import com.smartclassroom.backend.model.Session;
import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.repository.ODRequestRepository;
import com.smartclassroom.backend.repository.SessionRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentODService {

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private SessionRepository sessionRepository;

        @Autowired
        private ODRequestRepository odRequestRepository;

        @Transactional
        public void applyForOD(String username, StudentODRequestDTO requestDTO) {
                Student student = studentRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Student profile not found"));

                // Session validation removed. Using Date directly.
                java.time.LocalDate date = requestDTO.getDate();
                if (date == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is required");
                }

                // Validate: Duplicate check
                if (odRequestRepository.findByStudentIdAndDate(student.getId(), date).isPresent()) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                        "OD Request already exists for this date");
                }

                ODRequest odRequest = ODRequest.builder()
                                .student(student)
                                // .session(null)
                                .date(date)
                                .reason(requestDTO.getReason())
                                .status("PENDING")
                                .requestedAt(LocalDateTime.now())
                                .build();

                odRequestRepository.save(odRequest);
        }

        @Transactional(readOnly = true)
        public List<StudentODHistoryDTO> getODHistory(String username) {
                Student student = studentRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Student profile not found"));

                return odRequestRepository.findByStudentId(student.getId()).stream()
                                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())) // Sort desc
                                .map(od -> StudentODHistoryDTO.builder()
                                                .id(od.getId())
                                                .subjectName("Daily Attendance") // Generic
                                                // .sessionDate(null)
                                                .date(od.getDate())
                                                .reason(od.getReason())
                                                .status(od.getStatus())
                                                .requestedAt(od.getRequestedAt())
                                                .decisionAt(od.getDecisionAt())
                                                .build())
                                .collect(Collectors.toList());
        }
}
