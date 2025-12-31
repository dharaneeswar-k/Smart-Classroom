package com.smartclassroom.backend.controller.attendance;

import com.smartclassroom.backend.dto.attendance.PresenceEventIngestDTO;
import com.smartclassroom.backend.service.attendance.PresenceEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presence-event")
public class PresenceEventController {

    @Autowired
    private PresenceEventService presenceEventService;

    @PostMapping
    public ResponseEntity<String> ingestEvent(@RequestBody PresenceEventIngestDTO eventDTO) {
        presenceEventService.ingestEvent(eventDTO);
        return ResponseEntity.ok("Event processed");
    }
}
