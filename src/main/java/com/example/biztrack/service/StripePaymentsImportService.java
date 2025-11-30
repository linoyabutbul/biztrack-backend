package com.example.biztrack.service;

import com.example.biztrack.model.*;
import com.example.biztrack.repository.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class StripePaymentsImportService {

    private final ClientRepository clientRepository;
    private final IncomeRepository incomeRepository;

    public StripePaymentsImportService(ClientRepository clientRepository,
                                       IncomeRepository incomeRepository) {
        this.clientRepository = clientRepository;
        this.incomeRepository = incomeRepository;
    }

    public StripeImportResult importStripePayments(MultipartFile file) {

        int totalRows = 0;
        int imported = 0;
        int skippedNoClient = 0;
        int skippedNotSucceeded = 0;

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                    .setHeader()              // השורה הראשונה היא כותרת
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            for (CSVRecord record : records) {
                totalRows++;

                String status = safeGet(record, "Status");
                if (!(status.equalsIgnoreCase("Paid") || status.equalsIgnoreCase("succeeded"))) {
                    skippedNotSucceeded++;
                    continue;
                }

                String email = safeGet(record, "Customer Email");
                if (email.isBlank()) {
                    skippedNoClient++;
                    continue;
                }

                // חיפוש לקוח לפי email
                Optional<Client> clientOpt = clientRepository.findByEmailIgnoreCase(email);
                Client client;

                if (clientOpt.isPresent()) {
                    client = clientOpt.get();
                } else {
                    // יצירת לקוח חדש מה-CSV
                    client = new Client();

                    // קריאת שם לקוח מהקובץ
                    String name = safeGet(record, "Customer Name");
                    if (name.isBlank()) {
                        name = email; // fallback
                    }

                    client.setName(name);
                    client.setEmail(email);
                    client.setPhone("");  // אם אין מספר בקובץ
                    client.setCity("");   // אם אין עיר בקובץ
                    client.setNotes("Created automatically from Stripe payment");

                    client = clientRepository.save(client);
                }


                String amountStr = safeGet(record, "Amount");
                if (amountStr.isBlank()) {
                    continue;
                }
                BigDecimal amount = new BigDecimal(amountStr);

                String created = safeGet(record, "Created date (UTC)");
                LocalDate date = null;
                if (!created.isBlank() && created.length() >= 10) {
                    date = LocalDate.parse(created.substring(0, 10)); // YYYY-MM-DD
                }

                String description = safeGet(record, "Description");
                if (description.isBlank()) {
                    description = "Stripe payment for client " + client.getName();
                }

                Income income = new Income();
                income.setClient(client);
                income.setAmount(amount);
                income.setDate(date);
                income.setDescription(description);
                income.setSource("STRIPE");
                income.setIncomeType("CLIENT_PAYMENT");
                // אם יש לך category ב-Income – אפשר לשים:
                income.setCategory("CLIENT_PAYMENT");

                incomeRepository.save(income);
                imported++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import Stripe payments: " + e.getMessage(), e);
        }

        return new StripeImportResult(totalRows, imported, skippedNoClient, skippedNotSucceeded);
    }

    private String safeGet(CSVRecord record, String header) {
        try {
            String value = record.get(header);
            return value != null ? value.trim() : "";
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    public static class StripeImportResult {
        private final int totalRows;
        private final int imported;
        private final int skippedNoClient;
        private final int skippedNotSucceeded;

        public StripeImportResult(int totalRows, int imported, int skippedNoClient, int skippedNotSucceeded) {
            this.totalRows = totalRows;
            this.imported = imported;
            this.skippedNoClient = skippedNoClient;
            this.skippedNotSucceeded = skippedNotSucceeded;
        }

        public int getTotalRows() { return totalRows; }
        public int getImported() { return imported; }
        public int getSkippedNoClient() { return skippedNoClient; }
        public int getSkippedNotSucceeded() { return skippedNotSucceeded; }
    }
}
