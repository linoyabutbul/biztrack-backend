package com.example.biztrack.controller;

import com.example.biztrack.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class BankImportController {

    private final BankCsvImportService bankCsvImportService;

    public BankImportController(BankCsvImportService bankCsvImportService) {
        this.bankCsvImportService = bankCsvImportService;
    }

    @PostMapping("/bank-transactions")
    public ResponseEntity<String> importBankTransactions(@RequestParam("file") MultipartFile file) {
        int imported = bankCsvImportService.importBankCsv(file);
        return ResponseEntity.ok("Imported " + imported + " bank transactions (expenses + incomes)");
    }
}
