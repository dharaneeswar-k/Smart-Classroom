package com.smartclassroom.backend.controller.ai;

import com.smartclassroom.backend.dto.ai.AiBootstrapDTO;
import com.smartclassroom.backend.dto.ai.AiEventWrapperDTO;
import com.smartclassroom.backend.service.AiIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiIntegrationController {

    private final AiIntegrationService aiIntegrationService;

    @Value("${ai.api.key}")
    private String aiApiKey;

    private boolean isValidApiKey(String apiKey) {
        return aiApiKey != null && aiApiKey.equals(apiKey);
    }

    @GetMapping("/bootstrap")
    public ResponseEntity<AiBootstrapDTO> getBootstrapData(@RequestHeader("X-API-KEY") String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(aiIntegrationService.getBootstrapData());
    }

    @PostMapping("/events")
    public ResponseEntity<Void> receiveEvents(
            @RequestHeader("X-API-KEY") String apiKey,
            @RequestBody AiEventWrapperDTO eventWrapper) {

        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        aiIntegrationService.processEvents(eventWrapper);
        return ResponseEntity.ok().build();
    }
}
