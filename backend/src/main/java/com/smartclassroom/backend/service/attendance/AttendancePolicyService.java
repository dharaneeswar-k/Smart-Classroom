package com.smartclassroom.backend.service.attendance;

import com.smartclassroom.backend.model.AttendancePolicy;
import com.smartclassroom.backend.repository.AttendancePolicyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AttendancePolicyService {

    @Autowired
    private AttendancePolicyRepository attendancePolicyRepository;

    private AttendancePolicy activePolicy;

    @PostConstruct
    public void loadActivePolicy() {
        this.activePolicy = attendancePolicyRepository.findByIsActiveTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No active attendance policy configuration found."));
    }

    public AttendancePolicy getActivePolicy() {
        if (this.activePolicy == null) {
            loadActivePolicy();
        }
        if (this.activePolicy == null) {
            throw new RuntimeException("CRITICAL: No active attendance policy loaded. System cannot proceed.");
        }
        return this.activePolicy;
    }

    // Simple refresh mechanism if needed later
    public void refreshPolicy() {
        loadActivePolicy();
    }
}
