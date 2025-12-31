package com.smartclassroom.backend.service.admin;

import com.smartclassroom.backend.dto.admin.ClassroomDTO;
import com.smartclassroom.backend.model.Classroom;
import com.smartclassroom.backend.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public ClassroomDTO createClassroom(ClassroomDTO dto) {
        Classroom classroom = Classroom.builder()
                .classroomCode(dto.getClassroomCode())
                .classroomName(dto.getClassroomName())
                .department(dto.getDepartment())
                .createdAt(LocalDateTime.now())
                .build();

        Classroom saved = classroomRepository.save(classroom);
        return mapToDTO(saved);
    }

    public List<ClassroomDTO> getAllClassrooms() {
        return classroomRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ClassroomDTO updateClassroom(Long id, ClassroomDTO dto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Classroom not found"));

        classroom.setClassroomCode(dto.getClassroomCode());
        classroom.setClassroomName(dto.getClassroomName());
        classroom.setDepartment(dto.getDepartment());

        Classroom saved = classroomRepository.save(classroom);
        return mapToDTO(saved);
    }

    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id); // Hard delete for now as soft delete wasn't strictly schematized
    }

    private ClassroomDTO mapToDTO(Classroom classroom) {
        return ClassroomDTO.builder()
                .id(classroom.getId())
                .classroomCode(classroom.getClassroomCode())
                .classroomName(classroom.getClassroomName())
                .department(classroom.getDepartment())
                .build();
    }
}
