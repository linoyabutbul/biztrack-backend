package com.example.biztrack.controller;

import com.example.biztrack.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/ghl")
public class GhlWebhookController {

    private static final Logger log = LoggerFactory.getLogger(GhlWebhookController.class);

    private final AppointmentService appointmentService;
    private final String webhookSecret = System.getenv("GHL_WEBHOOK_SECRET");

    public GhlWebhookController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> receiveAppointment(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Webhook-Secret", required = false) String secretHeader
    ) {

        if (webhookSecret != null && !webhookSecret.isBlank()) {
            if (!webhookSecret.equals(secretHeader)) {
                log.warn("Invalid secret in GHL webhook. Header={}", secretHeader);
                return ResponseEntity.status(401).body("invalid secret");
            }
        }

        log.info("Received GHL webhook payload: {}", payload);

        String email = (String) payload.get("email");              // {{contact.email}}
        String externalId = (String) payload.get("opportunity_id"); // {{opportunity.id}}
        String pipeline = (String) payload.get("pipeline");         // {{opportunity.pipeline_name}} עדיף
        String stage = (String) payload.get("stage");               // {{opportunity.stage_name}}

        log.info("GHL appointment: email={}, externalId={}, pipeline={}, stage={}",
                email, externalId, pipeline, stage);

        if (email == null || externalId == null) {
            log.warn("Missing required fields in GHL webhook: {}", payload);
            return ResponseEntity.badRequest().body("missing fields");
        }

        String appointmentTimeStr = OffsetDateTime.now().toString();
        String source = "GHL_WEBHOOK";

        appointmentService.createCompletedAppointment(
                email,
                externalId,
                source,
                appointmentTimeStr
        );

        return ResponseEntity.ok("Webhook received");
    }
}
