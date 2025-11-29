package com.example.biztrack.model;

import java.math.BigDecimal;

public class MeetingStatsReport {

    private BigDecimal totalRelevantExpenses;
    private BigDecimal totalClientPayments;
    private long totalMeetingsCompleted;

    private BigDecimal averageCostPerMeeting;
    private BigDecimal averageRevenuePerMeeting;
    private BigDecimal averageProfitPerMeeting;

    public MeetingStatsReport(BigDecimal totalRelevantExpenses,
                              BigDecimal totalClientPayments,
                              long totalMeetingsCompleted,
                              BigDecimal averageCostPerMeeting,
                              BigDecimal averageRevenuePerMeeting,
                              BigDecimal averageProfitPerMeeting) {
        this.totalRelevantExpenses = totalRelevantExpenses;
        this.totalClientPayments = totalClientPayments;
        this.totalMeetingsCompleted = totalMeetingsCompleted;
        this.averageCostPerMeeting = averageCostPerMeeting;
        this.averageRevenuePerMeeting = averageRevenuePerMeeting;
        this.averageProfitPerMeeting = averageProfitPerMeeting;
    }

    public BigDecimal getTotalRelevantExpenses() { return totalRelevantExpenses; }
    public BigDecimal getTotalClientPayments() { return totalClientPayments; }
    public long getTotalMeetingsCompleted() { return totalMeetingsCompleted; }
    public BigDecimal getAverageCostPerMeeting() { return averageCostPerMeeting; }
    public BigDecimal getAverageRevenuePerMeeting() { return averageRevenuePerMeeting; }
    public BigDecimal getAverageProfitPerMeeting() { return averageProfitPerMeeting; }
}
