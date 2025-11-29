package com.example.biztrack.model;

import java.math.BigDecimal;

public class MeetingPurchaseSummary {

    private Long purchaseId;
    private Long clientId;
    private String clientName;

    private Integer meetingsPurchased;
    private Integer meetingsCompleted;
    private Integer meetingsRemaining;

    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal amountRemaining;

    public MeetingPurchaseSummary(Long purchaseId,
                                  Long clientId,
                                  String clientName,
                                  Integer meetingsPurchased,
                                  Integer meetingsCompleted,
                                  Integer meetingsRemaining,
                                  BigDecimal totalAmount,
                                  BigDecimal amountPaid,
                                  BigDecimal amountRemaining) {

        this.purchaseId = purchaseId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.meetingsPurchased = meetingsPurchased;
        this.meetingsCompleted = meetingsCompleted;
        this.meetingsRemaining = meetingsRemaining;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.amountRemaining = amountRemaining;
    }

    // Getters בלבד (אין צורך ב-setters כאן)
    public Long getPurchaseId() {
        return purchaseId;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public Integer getMeetingsPurchased() {
        return meetingsPurchased;
    }

    public Integer getMeetingsCompleted() {
        return meetingsCompleted;
    }

    public Integer getMeetingsRemaining() {
        return meetingsRemaining;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }
}
