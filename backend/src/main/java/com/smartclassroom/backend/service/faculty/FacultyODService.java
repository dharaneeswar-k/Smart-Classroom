package com.smartclassroom.backend.service.faculty;

import com.smartclassroom.backend.dto.faculty.ODRequestSummaryDTO;
import com.smartclassroom.backend.model.Faculty;
import com.smartclassroom.backend.repository.FacultyClassroomRepository;
import com.smartclassroom.backend.repository.FacultyRepository;
import com.smartclassroom.backend.repository.ODRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyODService {

        @Autowired
        private FacultyRepository facultyRepository;

        @Autowired
        private FacultyClassroomRepository facultyClassroomRepository;

        @Autowired
        private ODRequestRepository odRequestRepository;

        @Transactional(readOnly = true)
        public List<ODRequestSummaryDTO> getODRequests(String username, LocalDate date, Long classroomId) {
                Faculty faculty = facultyRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Faculty profile not found"));

                boolean isAssigned = facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                                .anyMatch(fc -> fc.getClassroom().getId().equals(classroomId));

                if (!isAssigned) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "Access denied: Classroom not assigned to this faculty");
                }

                // OD is Date-based
                return odRequestRepository.findByDate(date).stream()
                                .filter(od -> od.getStudent().getClassroom() != null &&
                                                od.getStudent().getClassroom().getId().equals(classroomId))
                                .map(od -> ODRequestSummaryDTO.builder()
                                                .id(od.getId())
                                                .studentRollNo(od.getStudent().getStudentRollNo())
                                                .studentName(od.getStudent().getStudentName())
                                                .reason(od.getReason())
                                                .status(od.getStatus())
                                                .requestedAt(od.getRequestedAt())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<ODRequestSummaryDTO> getPendingODRequests(String username) {
                Faculty faculty = facultyRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Faculty profile not found"));

                // Get all assigned classroom IDs
                List<Long> classroomIds = facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                                .map(fc -> fc.getClassroom().getId())
                                .collect(Collectors.toList());

                // Find all pending ODs for students in these classrooms
                return odRequestRepository.findAll().stream()
                                .filter(od -> "PENDING".equals(od.getStatus()))
                                .filter(od -> od.getStudent().getClassroom() != null &&
                                                classroomIds.contains(od.getStudent().getClassroom().getId()))
                                .map(od -> ODRequestSummaryDTO.builder()
                                                .id(od.getId())
                                                .studentRollNo(od.getStudent().getStudentRollNo())
                                                .studentName(od.getStudent().getStudentName())
                                                .reason(od.getReason())
                                                .status(od.getStatus())
                                                .requestedAt(od.getRequestedAt())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional
        public void approveOD(String username, Long odId) {
                updateODStatus(username, odId, "APPROVED");
        }

        @Transactional
        public void rejectOD(String username, Long odId) {
                updateODStatus(username, odId, "REJECTED");
        }

        private void updateODStatus(String username, Long odId, String status) {
                Faculty faculty = facultyRepository.findByUserUsername(username)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Faculty profile not found"));

                var odRequest = odRequestRepository.findById(odId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "OD Request not found"));

                // Check ownership: Faculty must be assigned to Student's Classroom
                if (odRequest.getStudent().getClassroom() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Student is not assigned to any classroom");
                }

                boolean isAssigned = facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                                .anyMatch(fc -> fc.getClassroom().getId()
                                                .equals(odRequest.getStudent().getClassroom().getId()));

                if (!isAssigned) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
                }

                odRequest.setStatus(status);
                odRequest.setApprovedBy(faculty); // Track who approved it
                odRequest.setDecisionAt(java.time.LocalDateTime.now()); // Track when
                odRequestRepository.save(odRequest);
        }
}
