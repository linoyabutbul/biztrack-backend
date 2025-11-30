package com.example.biztrack.controller;

import com.example.biztrack.model.*;
import com.example.biztrack.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientBillingController {

    private final ClientBillingService clientBillingService;

    public ClientBillingController(ClientBillingService clientBillingService) {
        this.clientBillingService = clientBillingService;
    }

    @GetMapping("/{clientId}/billing-summary")
    public ClientBillingSummary getClientBillingSummary(@PathVariable Long clientId) {
        return clientBillingService.getClientBillingSummary(clientId);
    }

    // איחוד לקוחות
    @PostMapping("/merge")
    public void mergeClients(@RequestParam Long mainClientId,
                             @RequestParam Long duplicateClientId) {
        clientBillingService.mergeClients(mainClientId, duplicateClientId);
    }
}
