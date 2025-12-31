package com.smartclassroom.backend.service.attendance;

import com.smartclassroom.backend.dto.attendance.PresenceEventIngestDTO;
import com.smartclassroom.backend.model.*;
import com.smartclassroom.backend.repository.CameraRepository;
import com.smartclassroom.backend.repository.PresenceEventRepository;
import com.smartclassroom.backend.repository.SessionRepository;
import com.smartclassroom.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PresenceEventService {

        @Autowired
        private AttendancePolicyService attendancePolicyService;

        @Autowired
        private PresenceEventRepository presenceEventRepository;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private CameraRepository cameraRepository;

        @Autowired
        private SessionRepository sessionRepository;

        @Transactional
        public void ingestEvent(PresenceEventIngestDTO eventDTO) {
                // 1. Load active policy
                AttendancePolicy policy = attendancePolicyService.getActivePolicy();

                // 2. Validate confidence against policy
                if (eventDTO.getConfidenceScore() < policy.getConfidenceThreshold()) {
                        return;
                }

                // 3. Validate references (Must exist)
                Student student = studentRepository.findById(eventDTO.getStudentId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Invalid Student ID"));

                Camera camera = cameraRepository.findById(eventDTO.getCameraId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Invalid Camera ID"));

                // Session validation removed

                // 4. Persist Event
                PresenceEvent event = PresenceEvent.builder()
                                .student(student)
                                .camera(camera)
                                // .session(null)
                                .date(eventDTO.getTimestamp().toLocalDate())
                                .eventTime(eventDTO.getTimestamp())
                                .confidenceScore(eventDTO.getConfidenceScore())
                                .build();

                presenceEventRepository.save(event);
        }
}
