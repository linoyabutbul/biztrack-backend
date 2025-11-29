package com.example.biztrack.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardSummary {

    // טווח תאריכים (לא חובה – אפשר null)
    private LocalDate startDate;
    private LocalDate endDate;

    // כמות
    private long totalClients;
    private long totalMeetingPackages;
    private long totalMeetingsCompleted;

    // כסף
    private BigDecimal totalRelevantExpenses;   // הוצאות שמוגדרות כעלות פגישה
    private BigDecimal totalClientPayments;     // הכנסות מסוג CLIENT_PAYMENT

    // ממוצעים
    private BigDecimal averageCostPerMeeting;
    private BigDecimal averageRevenuePerMeeting;
    private BigDecimal averageProfitPerMeeting;

    public DashboardSummary(LocalDate startDate,
                            LocalDate endDate,
                            long totalClients,
                            long totalMeetingPackages,
                            long totalMeetingsCompleted,
                            BigDecimal totalRelevantExpenses,
                            BigDecimal totalClientPayments,
                            BigDecimal averageCostPerMeeting,
                            BigDecimal averageRevenuePerMeeting,
                            BigDecimal averageProfitPerMeeting) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalClients = totalClients;
        this.totalMeetingPackages = totalMeetingPackages;
        this.totalMeetingsCompleted = totalMeetingsCompleted;
        this.totalRelevantExpenses = totalRelevantExpenses;
        this.totalClientPayments = totalClientPayments;
        this.averageCostPerMeeting = averageCostPerMeeting;
        this.averageRevenuePerMeeting = averageRevenuePerMeeting;
        this.averageProfitPerMeeting = averageProfitPerMeeting;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public long getTotalClients() { return totalClients; }
    public long getTotalMeetingPackages() { return totalMeetingPackages; }
    public long getTotalMeetingsCompleted() { return totalMeetingsCompleted; }
    public BigDecimal getTotalRelevantExpenses() { return totalRelevantExpenses; }
    public BigDecimal getTotalClientPayments() { return totalClientPayments; }
    public BigDecimal getAverageCostPerMeeting() { return averageCostPerMeeting; }
    public BigDecimal getAverageRevenuePerMeeting() { return averageRevenuePerMeeting; }
    public BigDecimal getAverageProfitPerMeeting() { return averageProfitPerMeeting; }
}
