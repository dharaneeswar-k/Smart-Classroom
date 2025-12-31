package com.smartclassroom.backend.service.integration;

import com.smartclassroom.backend.dto.PresenceEventRequest;

public interface PresenceEventService {
    void recordPresence(PresenceEventRequest request);
}
