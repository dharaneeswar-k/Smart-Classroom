package com.smartclassroom.backend.controller.integration;

import com.smartclassroom.backend.dto.PresenceEventRequest;
import com.smartclassroom.backend.service.integration.PresenceEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presence-events")
@RequiredArgsConstructor
public class IntegrationPresenceEventController {

    private final PresenceEventService presenceEventService;

    @PostMapping
    public ResponseEntity<Void> recordPresence(@RequestBody PresenceEventRequest request) {
        presenceEventService.recordPresence(request);
        return ResponseEntity.ok().build();
    }
}
