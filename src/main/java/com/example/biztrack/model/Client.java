package com.example.biztrack.model;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String notes;
    private String city;
    private Integer meetingsCount;
    private BigDecimal pricePerMeeting;

    // ✅ חובה – בנאי ריק בשביל JPA/Jackson
    public Client() {
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Integer getMeetingsCount() {
        return meetingsCount;
    }
    public void setMeetingsCount(Integer meetingsCount) {
        this.meetingsCount = meetingsCount;
    }
    public BigDecimal getPricePerMeeting() { return pricePerMeeting; }
    public void setPricePerMeeting(BigDecimal pricePerMeeting) { this.pricePerMeeting = pricePerMeeting; }

}
