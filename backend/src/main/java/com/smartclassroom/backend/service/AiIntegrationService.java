package com.smartclassroom.backend.service;

import com.smartclassroom.backend.dto.ai.*;
import com.smartclassroom.backend.model.Camera;
import com.smartclassroom.backend.model.Classroom;
import com.smartclassroom.backend.model.PresenceEvent;
import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.repository.CameraRepository;
import com.smartclassroom.backend.repository.ClassroomRepository;
import com.smartclassroom.backend.repository.PresenceEventRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiIntegrationService {

    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;
    private final CameraRepository cameraRepository;
    private final PresenceEventRepository presenceEventRepository;

    public AiBootstrapDTO getBootstrapData() {
        List<AiStudentDTO> students = studentRepository.findAll().stream()
                .map(student -> AiStudentDTO.builder()
                        .studentId(student.getStudentRollNo())
                        .classroomId(student.getClassroom() != null ? student.getClassroom().getClassroomCode() : null)
                        .build())
                .collect(Collectors.toList());

        List<AiClassroomDTO> classrooms = classroomRepository.findAll().stream()
                .flatMap(ctx -> cameraRepository.findAll().stream()
                        .filter(cam -> cam.getClassroom() != null && cam.getClassroom().getId().equals(ctx.getId()))
                        .map(cam -> AiClassroomDTO.builder()
                                .classroomId(ctx.getClassroomCode())
                                .cameraId(cam.getCameraCode())
                                .build()))
                .distinct()
                .collect(Collectors.toList());

        // Note: The structure requested was {classrooms: [{classroomId, cameraId}]}
        // which implies a join or mapping.
        // However, if a classroom has multiple cameras, it will appear multiple times
        // or we need a different structure.
        // The implementation plan used the flat mapping as per the requirement "THREE
        // TABLES".
        // If the requirement meant unique classrooms and mapping elsewhere, the
        // provided JSON structure
        // "classrooms": [{ "classroomId": "...", "cameraId": "..." }] suggests a
        // linking table format.
        // So strict following of the example: unique rows of classroom-camera pairs.

        List<AiCameraDTO> cameras = cameraRepository.findAll().stream()
                .map(cam -> AiCameraDTO.builder()
                        .cameraId(cam.getCameraCode())
                        .streamUrl(cam.getCameraHttpUrl())
                        .build())
                .collect(Collectors.toList());

        return AiBootstrapDTO.builder()
                .students(students)
                .classrooms(classrooms)
                .cameras(cameras)
                .build();
    }

    @Transactional
    public void processEvents(AiEventWrapperDTO eventWrapper) {
        if (eventWrapper.getEvents() == null || eventWrapper.getEvents().isEmpty()) {
            return;
        }

        // Pre-fetch references to avoid N+1 queries
        // Map Camera Code -> Camera Entity
        Map<String, Camera> cameraMap = cameraRepository.findAll().stream()
                .collect(Collectors.toMap(Camera::getCameraCode, Function.identity()));

        // Map Student Roll -> Student Entity
        Map<String, Student> studentMap = studentRepository.findAll().stream()
                .collect(Collectors.toMap(Student::getStudentRollNo, Function.identity()));

        for (AiPresenceEventDTO eventDto : eventWrapper.getEvents()) {
            Student student = studentMap.get(eventDto.getStudentId());
            Camera camera = cameraMap.get(eventDto.getCameraId());

            if (student != null && camera != null) {
                // Parse timestamp
                LocalDateTime eventTime;
                try {
                    // Try parsing ISO8601
                    eventTime = LocalDateTime.parse(eventDto.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception e) {
                    // Fallback or skip
                    System.err.println("Failed to parse timestamp for event: " + eventDto);
                    continue;
                }

                PresenceEvent event = PresenceEvent.builder()
                        .student(student)
                        .camera(camera)
                        .date(eventTime.toLocalDate())
                        .eventTime(eventTime)
                        .confidenceScore(eventDto.getConfidenceScore())
                        .build();

                presenceEventRepository.save(event);
            } else {
                // Log warning but don't fail batch
                System.out.println("Unknown entity in event: " + eventDto);
            }
        }
    }
}
