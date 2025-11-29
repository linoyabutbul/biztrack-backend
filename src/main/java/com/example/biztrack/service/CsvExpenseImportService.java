package com.example.biztrack.service;

import com.example.biztrack.model.Expense;
import com.example.biztrack.repository.ExpenseRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
public class CsvExpenseImportService {

    private final ExpenseRepository expenseRepository;

    public CsvExpenseImportService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public int importExpenses(MultipartFile file) {
        int count = 0;

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                    .setHeader()              // השורה הראשונה היא כותרת
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            for (CSVRecord record : records) {
                if (record.size() == 0) continue; // דילוג על שורות ריקות

                Expense expense = new Expense();

                String description = record.get("description");
                expense.setDescription(description);

                String category = safeGet(record, "category");
                expense.setCategory(category);

                String paymentMethod = safeGet(record, "paymentMethod");
                expense.setPaymentMethod(paymentMethod);

                String amountStr = safeGet(record, "amount");
                if (!amountStr.isBlank()) {
                    expense.setAmount(new BigDecimal(amountStr));
                }

                String dateStr = safeGet(record, "date");
                if (!dateStr.isBlank()) {
                    expense.setDate(LocalDate.parse(dateStr)); // פורמט YYYY-MM-DD
                }

                expenseRepository.save(expense);
                count++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import CSV: " + e.getMessage(), e);
        }

        return count;
    }

    private String safeGet(CSVRecord record, String header) {
        try {
            String value = record.get(header);
            return value != null ? value.trim() : "";
        } catch (IllegalArgumentException e) {
            return "";
        }
    }
}
