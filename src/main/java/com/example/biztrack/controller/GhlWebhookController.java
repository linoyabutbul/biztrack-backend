package com.example.biztrack.controller;

import com.example.biztrack.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ghl")
public class GhlWebhookController {

    private final AppointmentService appointmentService;

    public GhlWebhookController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<String> receiveAppointment(@RequestBody Map<String, Object> payload) {

        // מה שה-WebHook ב-GHL ישלח דרך Custom Data
        String email = (String) payload.get("email");  // {{contact.email}}
        String externalId = (String) payload.get("appointment_id"); // או opportunity_id
        String appointmentTimeStr = (String) payload.get("start_time");
        String source = "GHL_WEBHOOK";

        // אין 'status' ולכן אין בדיקה עליו
        appointmentService.createCompletedAppointment(
                email,
                externalId,
                source,
                appointmentTimeStr
        );

        return ResponseEntity.ok("Webhook received");
    }
}
