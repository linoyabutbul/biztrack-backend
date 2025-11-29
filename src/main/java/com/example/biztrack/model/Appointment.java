package com.example.biztrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // הלקוח במערכת שלך
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // מזהה חיצוני מה-GHL (הזדמנות/פגישה)
    private String externalId;

    // מאיפה זה הגיע (למשל: "GHL_OPPORTUNITY_FL")
    private String source;

    // מתי הפגישה קרתה בפועל
    private LocalDateTime happenedAt;

    public Appointment() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }

    public void setClient(Client client) { this.client = client; }

    public String getExternalId() { return externalId; }

    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    public LocalDateTime getHappenedAt() { return happenedAt; }

    public void setHappenedAt(LocalDateTime happenedAt) { this.happenedAt = happenedAt; }
}
