package com.smartclassroom.backend.service.integration;

import com.smartclassroom.backend.dto.PresenceEventRequest;
import com.smartclassroom.backend.model.Camera;
import com.smartclassroom.backend.model.PresenceEvent;
import com.smartclassroom.backend.model.Student;
import com.smartclassroom.backend.repository.CameraRepository;
import com.smartclassroom.backend.repository.PresenceEventRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PresenceEventServiceImpl implements PresenceEventService {

    private final PresenceEventRepository presenceEventRepository;
    private final StudentRepository studentRepository;
    private final CameraRepository cameraRepository;

    @Override
    @Transactional
    public void recordPresence(PresenceEventRequest request) {
        // 1. Resolve Student (Assuming request.getStudentId() sends the Roll No or
        // Filename-ID)
        // Adjust logic based on what Python actually sends.
        // If Python sends "student_filename", we might need to map it.
        // For now, assuming request.studentId matches a Student Roll No or ID.
        // Let's assume Python sends the Roll Number or a unique identifier we can look
        // up.

        // IMPORTANT: In a real scenario, cache this lookup.
        Student student = studentRepository.findByStudentRollNo(request.getStudentId())
                .orElse(null);

        // If not found by Roll No, try ID if it's numeric? Or just log warning.
        // For robustness, let's assume valid student for now or let it fail gracefully.
        if (student == null) {
            // Try matching faceImagePath filename if needed?
            // For now, we'll assume the Python sends the 'studentRollNo'.
            System.err.println("Student not found for ID: " + request.getStudentId());
            return;
        }

        // 2. Resolve Camera
        Camera camera = cameraRepository.findById(request.getCameraId())
                .orElseThrow(() -> new EntityNotFoundException("Camera not found"));

        // 3. Parse Timestamp
        LocalDateTime eventTime = LocalDateTime.parse(request.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);

        // 4. Create and Save Event
        PresenceEvent event = PresenceEvent.builder()
                .student(student)
                .camera(camera)
                .date(eventTime.toLocalDate())
                .eventTime(eventTime)
                .confidenceScore(request.getConfidenceScore())
                .build();

        presenceEventRepository.save(event);
    }
}
