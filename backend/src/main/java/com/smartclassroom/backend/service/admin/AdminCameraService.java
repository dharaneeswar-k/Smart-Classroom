package com.smartclassroom.backend.service.admin;

import com.smartclassroom.backend.dto.admin.CameraDTO;
import com.smartclassroom.backend.model.Camera;
import com.smartclassroom.backend.model.Classroom;
import com.smartclassroom.backend.repository.CameraRepository;
import com.smartclassroom.backend.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCameraService {

        @Autowired
        private CameraRepository cameraRepository;

        @Autowired
        private ClassroomRepository classroomRepository;

        public CameraDTO addCamera(CameraDTO dto) {
                if (dto.getClassroomId() == null) {
                        throw new org.springframework.web.server.ResponseStatusException(
                                        org.springframework.http.HttpStatus.BAD_REQUEST,
                                        "ClassroomId is required for Camera creation");
                }

                Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND,
                                                "Classroom not found with ID: " + dto.getClassroomId()));

                Camera camera = Camera.builder()
                                .cameraCode(dto.getCameraCode())
                                .cameraHttpUrl(dto.getCameraHttpUrl())
                                .status(dto.getStatus())
                                .classroom(classroom)
                                .build();

                Camera saved = cameraRepository.save(camera);
                return mapToDTO(saved);
        }

        public List<CameraDTO> getCamerasByClassroom(Long classroomId) {
                // Since Classroom entity doesn't have OneToMany list in strict schema,
                // we fetch all cameras and filter by classroomId.
                return cameraRepository.findAll().stream()
                                .filter(c -> c.getClassroom() != null && c.getClassroom().getId().equals(classroomId))
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        }

        public List<CameraDTO> getAllCameras() {
                return cameraRepository.findAll().stream()
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        }

        public CameraDTO updateCamera(Long id, CameraDTO dto) {
                Camera camera = cameraRepository.findById(id)
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND, "Camera not found"));

                if (dto.getCameraCode() != null && !dto.getCameraCode().isEmpty()) {
                        camera.setCameraCode(dto.getCameraCode());
                }
                if (dto.getCameraHttpUrl() != null && !dto.getCameraHttpUrl().isEmpty()) {
                        camera.setCameraHttpUrl(dto.getCameraHttpUrl());
                }
                if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                        camera.setStatus(dto.getStatus());
                }
                if (dto.getClassroomId() != null) {
                        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                                        .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                        org.springframework.http.HttpStatus.NOT_FOUND,
                                                        "Classroom not found with ID: " + dto.getClassroomId()));
                        camera.setClassroom(classroom);
                }

                Camera saved = cameraRepository.save(camera);
                return mapToDTO(saved);
        }

        public void deleteCamera(Long id) {
                if (!cameraRepository.existsById(id)) {
                        throw new org.springframework.web.server.ResponseStatusException(
                                        org.springframework.http.HttpStatus.NOT_FOUND, "Camera not found");
                }
                cameraRepository.deleteById(id);
        }

        private CameraDTO mapToDTO(Camera camera) {
                return CameraDTO.builder()
                                .id(camera.getId())
                                .cameraCode(camera.getCameraCode())
                                .cameraHttpUrl(camera.getCameraHttpUrl())
                                .status(camera.getStatus())
                                .classroomId(camera.getClassroom().getId())
                                .build();
        }
}
