package com.example.biztrack.model;

import java.math.BigDecimal;

public class ClientBillingSummary {

    private Long clientId;
    private String clientName;
    private String city;

    private int totalMeetingsPurchased;
    private int totalMeetingsCompleted;
    private int totalMeetingsRemaining;

    private BigDecimal totalClientPayments;
    private BigDecimal averageRevenuePerMeetingForClient;

    private BigDecimal globalAverageCostPerMeeting;
    private BigDecimal globalAverageProfitPerMeeting;

    public ClientBillingSummary(Long clientId,
                                String clientName,
                                String city,
                                int totalMeetingsPurchased,
                                int totalMeetingsCompleted,
                                int totalMeetingsRemaining,
                                BigDecimal totalClientPayments,
                                BigDecimal averageRevenuePerMeetingForClient,
                                BigDecimal globalAverageCostPerMeeting,
                                BigDecimal globalAverageProfitPerMeeting) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.city = city;
        this.totalMeetingsPurchased = totalMeetingsPurchased;
        this.totalMeetingsCompleted = totalMeetingsCompleted;
        this.totalMeetingsRemaining = totalMeetingsRemaining;
        this.totalClientPayments = totalClientPayments;
        this.averageRevenuePerMeetingForClient = averageRevenuePerMeetingForClient;
        this.globalAverageCostPerMeeting = globalAverageCostPerMeeting;
        this.globalAverageProfitPerMeeting = globalAverageProfitPerMeeting;
    }

    public Long getClientId() { return clientId; }
    public String getClientName() { return clientName; }
    public String getCity() { return city; }
    public int getTotalMeetingsPurchased() { return totalMeetingsPurchased; }
    public int getTotalMeetingsCompleted() { return totalMeetingsCompleted; }
    public int getTotalMeetingsRemaining() { return totalMeetingsRemaining; }
    public BigDecimal getTotalClientPayments() { return totalClientPayments; }
    public BigDecimal getAverageRevenuePerMeetingForClient() { return averageRevenuePerMeetingForClient; }
    public BigDecimal getGlobalAverageCostPerMeeting() { return globalAverageCostPerMeeting; }
    public BigDecimal getGlobalAverageProfitPerMeeting() { return globalAverageProfitPerMeeting; }
}
