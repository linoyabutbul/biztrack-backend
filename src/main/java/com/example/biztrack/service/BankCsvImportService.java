package com.example.biztrack.service;

import com.example.biztrack.model.Expense;
import com.example.biztrack.model.Income;
import com.example.biztrack.repository.ExpenseRepository;
import com.example.biztrack.repository.IncomeRepository;
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
public class BankCsvImportService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public BankCsvImportService(ExpenseRepository expenseRepository,
                                IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public int importBankCsv(MultipartFile file) {
        int count = 0;

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                    .setHeader()              // השורה הראשונה ככותרת
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            for (CSVRecord record : records) {

                String status = safeGet(record, "Status");
                if (!"COMPLETED".equalsIgnoreCase(status)) {
                    continue; // מדלגים על טרנזקציות לא גמורות
                }

                String direction = safeGet(record, "Direction"); // OUT / IN
                String createdOn = safeGet(record, "Created on"); // "2025-11-24 19:18:58"
                String sourceAmountStr = safeGet(record, "Source amount (after fees)");
                if (sourceAmountStr.isBlank()) {
                    sourceAmountStr = safeGet(record, "Source amount");}
                String categoryRaw = safeGet(record, "Category");
                String sourceName = safeGet(record, "Source name");
                String targetName = safeGet(record, "Target name");

                if (sourceAmountStr.isBlank()) {
                    continue;
                }

                BigDecimal amount = new BigDecimal(sourceAmountStr);

                // נפריד תאריך מהשדה "Created on"
                LocalDate date = null;
                if (!createdOn.isBlank() && createdOn.length() >= 10) {
                    date = LocalDate.parse(createdOn.substring(0, 10)); // YYYY-MM-DD
                }

                String mappedCategory = mapCategory(categoryRaw, sourceName, targetName);

                if ("OUT".equalsIgnoreCase(direction)) {
                    // הוצאה
                    Expense expense = new Expense();
                    expense.setAmount(amount);
                    expense.setDate(date);
                    expense.setCategory(mappedCategory);
                    expense.setPaymentMethod("BANK/STRIPE");
                    expense.setDescription(buildDescriptionForExpense(sourceName, targetName, categoryRaw));

                    expenseRepository.save(expense);
                    count++;
                } else if ("IN".equalsIgnoreCase(direction)) {
                    // הכנסה
                    Income income = new Income();
                    income.setAmount(amount);
                    income.setDate(date);
                    income.setCategory(mappedCategory);
                    income.setSource("BANK/STRIPE");
                    income.setDescription(buildDescriptionForIncome(sourceName, targetName, categoryRaw));

                    incomeRepository.save(income);
                    count++;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import bank CSV: " + e.getMessage(), e);
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

    private String mapCategory(String rawCategory, String sourceName, String targetName) {
        String c = rawCategory != null ? rawCategory.toLowerCase() : "";
        String s = sourceName != null ? sourceName.toLowerCase() : "";
        String t = targetName != null ? targetName.toLowerCase() : "";

        // פרסום – פייסבוק / גוגל / מטא
        if (c.contains("marketing") || s.contains("facebook") || s.contains("meta") || t.contains("facebook") || t.contains("google")) {
            return "ADS_MARKETING";
        }

        // קבלנים / VA / עובדים חיצוניים
        if (c.contains("contract") || c.contains("services") || t.contains("va") || s.contains("va")) {
            return "STAFF_CONTRACTORS";
        }

        // עמלות Stripe / בנק
        if (c.contains("fee") || s.contains("stripe") && c.contains("general")) {
            return "FEES_STRIPE";
        }

        if (c.isBlank()) {
            return "GENERAL";
        }

        // כבר יש קטגוריה הגיונית – נחזיר אותה כמו שהיא
        return rawCategory;
    }

    private String buildDescriptionForExpense(String sourceName, String targetName, String rawCategory) {
        StringBuilder sb = new StringBuilder();

        if (!rawCategory.isBlank()) {
            sb.append(rawCategory).append(" - ");
        }
        if (!sourceName.isBlank()) {
            sb.append(sourceName);
        }
        if (!targetName.isBlank()) {
            sb.append(" → ").append(targetName);
        }

        return sb.toString();
    }

    private String buildDescriptionForIncome(String sourceName, String targetName, String rawCategory) {
        StringBuilder sb = new StringBuilder("Income");

        if (!rawCategory.isBlank()) {
            sb.append(" (").append(rawCategory).append(")");
        }
        if (!sourceName.isBlank()) {
            sb.append(" from ").append(sourceName);
        }
        if (!targetName.isBlank()) {
            sb.append(" to ").append(targetName);
        }

        return sb.toString();
    }
}
