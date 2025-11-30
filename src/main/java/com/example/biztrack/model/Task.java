package com.example.biztrack.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String status;
    private Integer subTasksCompleted;
    private Integer subTasksTotal;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate completedDate;
    private String priority;             // HIGH / NORMAL / LOW
    private String assignee;
    private String address;
    private String phone;
    private String email;
    @Column(length = 2000)
    private String notes;
    private String attachmentUrl;

    // --- GETTERS & SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;

        // אם עבר ל-DONE אז שומר את התאריך אוטומטית
        if ("DONE".equalsIgnoreCase(status)) {
            this.completedDate = LocalDate.now();
        }
    }

    public Integer getSubTasksCompleted() { return subTasksCompleted; }
    public void setSubTasksCompleted(Integer subTasksCompleted) { this.subTasksCompleted = subTasksCompleted; }

    public Integer getSubTasksTotal() { return subTasksTotal; }
    public void setSubTasksTotal(Integer subTasksTotal) { this.subTasksTotal = subTasksTotal; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
}
