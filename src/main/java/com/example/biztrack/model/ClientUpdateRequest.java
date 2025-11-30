package com.example.biztrack.model;

import java.math.BigDecimal;

public class ClientUpdateRequest {


    private String name;
    private String email;
    private String city;
    private String notes;
    private BigDecimal pricePerMeeting;

    public ClientUpdateRequest() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public BigDecimal getPricePerMeeting() { return pricePerMeeting; }
    public void setPricePerMeeting(BigDecimal pricePerMeeting) { this.pricePerMeeting = pricePerMeeting; }
}
