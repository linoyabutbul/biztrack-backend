package com.example.biztrack.controller;

import com.example.biztrack.service.StripePaymentsImportService;
import com.example.biztrack.service.StripePaymentsImportService.StripeImportResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")

public class StripeImportController {

    private final StripePaymentsImportService stripePaymentsImportService;

    public StripeImportController(StripePaymentsImportService stripePaymentsImportService) {
        this.stripePaymentsImportService = stripePaymentsImportService;
    }

    @PostMapping("/stripe-payments")
    public ResponseEntity<StripePaymentsImportService.StripeImportResult> importStripePayments(@RequestParam("file") MultipartFile file) {
        StripeImportResult result = stripePaymentsImportService.importStripePayments(file);
        return ResponseEntity.ok(result);
    }
}
