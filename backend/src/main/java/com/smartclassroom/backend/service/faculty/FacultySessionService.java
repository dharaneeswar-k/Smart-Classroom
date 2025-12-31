package com.smartclassroom.backend.service.faculty;

import com.smartclassroom.backend.dto.faculty.SessionSummaryDTO;
import com.smartclassroom.backend.model.Faculty;
import com.smartclassroom.backend.repository.FacultyClassroomRepository;
import com.smartclassroom.backend.repository.FacultyRepository;
import com.smartclassroom.backend.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultySessionService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyClassroomRepository facultyClassroomRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public List<SessionSummaryDTO> getSessions(String username, Long classroomId) {
        Faculty faculty = facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty profile not found"));

        boolean isAssigned = facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                .anyMatch(fc -> fc.getClassroom().getId().equals(classroomId));

        if (!isAssigned) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access denied: Classroom not assigned to this faculty");
        }

        return sessionRepository.findByClassroomId(classroomId).stream()
                .map(s -> SessionSummaryDTO.builder()
                        .id(s.getId())
                        .subjectName(s.getSubjectName())
                        .sessionDate(s.getSessionDate())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .status(s.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
