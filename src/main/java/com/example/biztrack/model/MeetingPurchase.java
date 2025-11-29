package com.example.biztrack.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "meeting_purchases")
public class MeetingPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // כמה פגישות בחבילה
    private Integer meetingsPurchased;

    // מחיר לפגישה
    private BigDecimal pricePerMeeting;

    // ערך מלא של החבילה (לא חייבים לשלוח – אפשר לחשב)
    private BigDecimal totalAmount;

    // תאריך רכישת החבילה
    private LocalDate purchaseDate;

    // 👇 חדשים
    // כמה פגישות כבר בוצעו ושולמו
    private Integer meetingsCompleted;

    // כמה כסף הלקוח שילם עד עכשיו על החבילה
    private BigDecimal amountPaid;

    public MeetingPurchase() {
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getMeetingsPurchased() {
        return meetingsPurchased;
    }

    public void setMeetingsPurchased(Integer meetingsPurchased) {
        this.meetingsPurchased = meetingsPurchased;
    }

    public BigDecimal getPricePerMeeting() {
        return pricePerMeeting;
    }

    public void setPricePerMeeting(BigDecimal pricePerMeeting) {
        this.pricePerMeeting = pricePerMeeting;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getMeetingsCompleted() {
        return meetingsCompleted;
    }

    public void setMeetingsCompleted(Integer meetingsCompleted) {
        this.meetingsCompleted = meetingsCompleted;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}
