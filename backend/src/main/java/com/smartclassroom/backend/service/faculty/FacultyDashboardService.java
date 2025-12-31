package com.smartclassroom.backend.service.faculty;

import com.smartclassroom.backend.dto.faculty.ClassroomSummaryDTO;
import com.smartclassroom.backend.model.Faculty;
import com.smartclassroom.backend.repository.FacultyClassroomRepository;
import com.smartclassroom.backend.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyDashboardService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyClassroomRepository facultyClassroomRepository;

    @Transactional(readOnly = true)
    public List<ClassroomSummaryDTO> getAssignedClassrooms(String username) {
        Faculty faculty = facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty profile not found"));

        return facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                .map(fc -> ClassroomSummaryDTO.builder()
                        .id(fc.getClassroom().getId())
                        .classroomCode(fc.getClassroom().getClassroomCode())
                        .classroomName(fc.getClassroom().getClassroomName())
                        .department(fc.getClassroom().getDepartment())
                        .build())
                .collect(Collectors.toList());
    }
}
