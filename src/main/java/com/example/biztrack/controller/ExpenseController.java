package com.example.biztrack.controller;

import com.example.biztrack.model.Expense;
import com.example.biztrack.repository.ExpenseRepository;
import com.example.biztrack.service.CsvExpenseImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final CsvExpenseImportService csvExpenseImportService;

    public ExpenseController(ExpenseRepository expenseRepository,
                             CsvExpenseImportService csvExpenseImportService) {
        this.expenseRepository = expenseRepository;
        this.csvExpenseImportService = csvExpenseImportService;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importExpensesFromCsv(@RequestParam("file") MultipartFile file) {
        int imported = csvExpenseImportService.importExpenses(file);
        return ResponseEntity.ok("Imported " + imported + " expenses from CSV");
    }
}
