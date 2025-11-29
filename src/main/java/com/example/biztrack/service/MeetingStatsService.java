package com.example.biztrack.service;

import com.example.biztrack.model.*;
import com.example.biztrack.repository.ExpenseRepository;
import com.example.biztrack.repository.IncomeRepository;
import com.example.biztrack.repository.MeetingPurchaseRepository;
import com.example.biztrack.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingStatsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final MeetingPurchaseRepository meetingPurchaseRepository;
    private final ClientRepository clientRepository;

    public MeetingStatsService(ExpenseRepository expenseRepository,
                               IncomeRepository incomeRepository,
                               MeetingPurchaseRepository meetingPurchaseRepository,
                               ClientRepository clientRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.meetingPurchaseRepository = meetingPurchaseRepository;
        this.clientRepository = clientRepository;
    }

    // 🔹 1. סטטיסטיקה כללית ללא פילטר תאריכים
    public MeetingStatsReport calculateStats() {
        return calculateStats(null, null, null, null);
    }

    // 🔹 2. סטטיסטיקה לפי טווח תאריכים
    public MeetingStatsReport calculateStats(LocalDate startDate, LocalDate endDate) {
        return calculateStats(startDate, endDate, null, null);
    }

    // 🔹 3. סטטיסטיקה ללקוח
    public MeetingStatsReport calculateStatsForClient(Long clientId,
                                                      LocalDate startDate,
                                                      LocalDate endDate) {
        return calculateStats(startDate, endDate, clientId, null);
    }

    // 🔹 4. סטטיסטיקה לפי עיר
    public MeetingStatsReport calculateStatsForCity(String city,
                                                    LocalDate startDate,
                                                    LocalDate endDate) {
        return calculateStats(startDate, endDate, null, city);
    }

    // ⭐ הפונקציה המרכזית – מקבלת פילטרים ורצה עליהם
    private MeetingStatsReport calculateStats(LocalDate startDate,
                                              LocalDate endDate,
                                              Long clientId,
                                              String city) {

        // 1) כל ההוצאות
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> filteredExpenses = filterExpenses(allExpenses, startDate, endDate);

        BigDecimal totalRelevantExpenses = filteredExpenses.stream()
                .filter(this::isMeetingCostExpense) // רק שכר עובדות + פרסום META
                .map(exp -> exp.getAmount() != null ? exp.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2) כל ה-MeetingPurchase
        List<MeetingPurchase> allPurchases = meetingPurchaseRepository.findAll();

        // אם יש city – נבחר רק לקוחות מהעיר הזאת
        if (city != null && !city.isBlank()) {
            String cityLower = city.toLowerCase();
            allPurchases = allPurchases.stream()
                    .filter(mp -> mp.getClient() != null &&
                            mp.getClient().getCity() != null &&
                            mp.getClient().getCity().toLowerCase().equals(cityLower))
                    .collect(Collectors.toList());
        }

        // אם יש clientId – נבחר רק את הלקוח הזה
        if (clientId != null) {
            allPurchases = allPurchases.stream()
                    .filter(mp -> mp.getClient() != null &&
                            clientId.equals(mp.getClient().getId()))
                    .collect(Collectors.toList());
        }

        List<MeetingPurchase> filteredPurchases = filterMeetingPurchases(allPurchases, startDate, endDate);

        long totalMeetingsCompleted = filteredPurchases.stream()
                .mapToLong(mp -> mp.getMeetingsCompleted() != null ? mp.getMeetingsCompleted() : 0)
                .sum();

        // 3) כל ההכנסות
        List<Income> allIncomes = incomeRepository.findAll();

        // client / city filter להכנסות
        if (city != null && !city.isBlank()) {
            String cityLower = city.toLowerCase();
            allIncomes = allIncomes.stream()
                    .filter(in -> in.getClient() != null &&
                            in.getClient().getCity() != null &&
                            in.getClient().getCity().toLowerCase().equals(cityLower))
                    .collect(Collectors.toList());
        }

        if (clientId != null) {
            allIncomes = allIncomes.stream()
                    .filter(in -> in.getClient() != null &&
                            clientId.equals(in.getClient().getId()))
                    .collect(Collectors.toList());
        }

        List<Income> filteredIncomes = filterIncomes(allIncomes, startDate, endDate);

        BigDecimal totalClientPayments = filteredIncomes.stream()
                .filter(in -> "CLIENT_PAYMENT".equalsIgnoreCase(in.getIncomeType()))
                .map(in -> in.getAmount() != null ? in.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalMeetingsCompleted == 0) {
            return new MeetingStatsReport(
                    totalRelevantExpenses,
                    totalClientPayments,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        BigDecimal meetingsCount = BigDecimal.valueOf(totalMeetingsCompleted);

        BigDecimal averageCostPerMeeting = totalRelevantExpenses
                .divide(meetingsCount, 2, RoundingMode.HALF_UP);

        BigDecimal averageRevenuePerMeeting = totalClientPayments
                .divide(meetingsCount, 2, RoundingMode.HALF_UP);

        BigDecimal averageProfitPerMeeting = averageRevenuePerMeeting
                .subtract(averageCostPerMeeting);

        return new MeetingStatsReport(
                totalRelevantExpenses,
                totalClientPayments,
                totalMeetingsCompleted,
                averageCostPerMeeting,
                averageRevenuePerMeeting,
                averageProfitPerMeeting
        );
    }

    // 💸 האם ההוצאה הזו נחשבת "עלות פגישה"?
    // לפי מה שסיכמנו: שכר העובדות + פרסום META בלבד
    private boolean isMeetingCostExpense(Expense expense) {
        String category = safeLower(expense.getCategory());
        String description = safeLower(expense.getDescription());

        // שכר עובדות / קבלניות
        if (category.contains("staff_contractors") || category.contains("staff")) {
            return true;
        }

        // פרסום META (פייסבוק/מטא)
        if (category.contains("ads_marketing") || category.contains("marketing")) {
            if (description.contains("facebook") || description.contains("meta")) {
                return true;
            }
        }

        return false;
    }

    private String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    private List<Expense> filterExpenses(List<Expense> list, LocalDate start, LocalDate end) {
        if (start == null && end == null) return list;
        return list.stream()
                .filter(e -> {
                    LocalDate d = e.getDate();
                    if (d == null) return false;
                    if (start != null && d.isBefore(start)) return false;
                    if (end != null && d.isAfter(end)) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }

    private List<Income> filterIncomes(List<Income> list, LocalDate start, LocalDate end) {
        if (start == null && end == null) return list;
        return list.stream()
                .filter(i -> {
                    LocalDate d = i.getDate();
                    if (d == null) return false;
                    if (start != null && d.isBefore(start)) return false;
                    if (end != null && d.isAfter(end)) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }

    private List<MeetingPurchase> filterMeetingPurchases(List<MeetingPurchase> list, LocalDate start, LocalDate end) {
        if (start == null && end == null) return list;
        return list.stream()
                .filter(mp -> {
                    LocalDate d = mp.getPurchaseDate();
                    if (d == null) return false;
                    if (start != null && d.isBefore(start)) return false;
                    if (end != null && d.isAfter(end)) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }
}
