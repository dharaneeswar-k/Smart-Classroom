package com.smartclassroom.backend.service.admin;

import com.smartclassroom.backend.dto.admin.FacultyDTO;
import com.smartclassroom.backend.model.Classroom;
import com.smartclassroom.backend.model.Faculty;
import com.smartclassroom.backend.model.FacultyClassroom;
import com.smartclassroom.backend.model.User;
import com.smartclassroom.backend.repository.ClassroomRepository;
import com.smartclassroom.backend.repository.FacultyClassroomRepository;
import com.smartclassroom.backend.repository.FacultyRepository;
import com.smartclassroom.backend.service.UserProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminFacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private UserProvisioningService userProvisioningService;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private FacultyClassroomRepository facultyClassroomRepository;

    @Transactional
    public FacultyDTO createFaculty(FacultyDTO dto) {
        // 1. Create User via Provisioning Service
        String password = (dto.getPassword() != null && !dto.getPassword().isEmpty()) ? dto.getPassword()
                : "faculty123";
        // Email is currently ignored by backend User entity but passed for future use
        User user = userProvisioningService.createUser(
                dto.getEmployeeId(), // As username
                password,
                dto.getEmail(),
                "FACULTY");

        // 2. Create Faculty
        Faculty faculty = Faculty.builder()
                .facultyName(dto.getFacultyName())
                .designation(dto.getDesignation())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Faculty savedFaculty = facultyRepository.save(faculty);

        // 3. Assign Classrooms if provided
        if (dto.getClassroomIds() != null && !dto.getClassroomIds().isEmpty()) {
            assignFacultyToClassrooms(savedFaculty.getId(), dto.getClassroomIds());
        }

        return mapToDTO(savedFaculty);
    }

    @Transactional
    public void assignFacultyToClassrooms(Long facultyId, List<Long> classroomIds) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Faculty not found"));

        List<Classroom> classrooms = classroomRepository.findAllById(classroomIds);

        for (Classroom classroom : classrooms) {
            FacultyClassroom assignment = FacultyClassroom.builder()
                    .faculty(faculty)
                    .classroom(classroom)
                    .assignedAt(LocalDateTime.now())
                    .build();
            facultyClassroomRepository.save(assignment);
        }
    }

    public List<FacultyDTO> getAllFaculty() {
        return facultyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FacultyDTO updateFaculty(Long id, FacultyDTO dto) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Faculty not found"));

        if (dto.getFacultyName() != null && !dto.getFacultyName().isEmpty()) {
            faculty.setFacultyName(dto.getFacultyName());
        }
        if (dto.getDesignation() != null && !dto.getDesignation().isEmpty()) {
            faculty.setDesignation(dto.getDesignation());
        }

        // Note: Updating User credentials (username/password) is not handled here to
        // keep it
        // safe.
        // Identify Classroom changes if provided
        if (dto.getClassroomIds() != null) {
            // Clear existing assignments? Or strictly overwrite?
            // "Assign" is simpler. Let's fully overwrite for "Update" semantics.
            facultyClassroomRepository.deleteByFacultyId(id); // Requires Custom Query or helper
            // Re-assign
            assignFacultyToClassrooms(id, dto.getClassroomIds());
        }

        Faculty saved = facultyRepository.save(faculty);
        return mapToDTO(saved);
    }

    public void deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Faculty not found");
        }
        facultyRepository.deleteById(id);
    }

    private FacultyDTO mapToDTO(Faculty faculty) {
        // Optimized: Use DB query instead of Loading ALL logic
        List<Long> classroomIds = facultyClassroomRepository.findByFacultyId(faculty.getId()).stream()
                .map(fc -> fc.getClassroom().getId())
                .collect(Collectors.toList());

        return FacultyDTO.builder()
                .id(faculty.getId())
                .employeeId(faculty.getUser() != null ? faculty.getUser().getUsername() : null)
                .facultyName(faculty.getFacultyName())
                .designation(faculty.getDesignation())
                .email(null) // Entity does not support Email yet
                .classroomIds(classroomIds)
                .build();
    }
}
