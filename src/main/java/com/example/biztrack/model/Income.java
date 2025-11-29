package com.example.biztrack.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal amount;

    private LocalDate date;

    private String source;         // מקור התשלום – STRIPE / BANK / INTERNAL

    private String incomeType;     // CLIENT_PAYMENT / STRIPE_PAYOUT / OTHER

    private String category;       // 🎯 חדש – כמו ב-Expense

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    public Income() {
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getIncomeType() { return incomeType; }
    public void setIncomeType(String incomeType) { this.incomeType = incomeType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}
