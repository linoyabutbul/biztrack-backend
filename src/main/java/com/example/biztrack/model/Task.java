package com.example.biztrack.model;
import jakarta.persistence.*;
import java.time.LocalDate;
// מידע מותאם אישית לכל משימה


@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Task name/title
    private String title;

    // Status: TODO / IN_PROGRESS / DONE
    private String status;

    // Subtasks: completed/total, e.g. 1/3
    private Integer subTasksCompleted;
    private Integer subTasksTotal;

    // Dates
    private LocalDate startDate;
    private LocalDate dueDate;

    // Who is responsible (for now just a name or email string)
    private String assignee;

    // For daily reset logic
    private LocalDate createdDate;
    private LocalDate completedDate; // when status becomes DONE
    private String address;        // כתובת (למשל לניווט)
    private String navUrl;         // URL מוכן ל-Waze/Google Maps
    private String phone;          // טלפון לחיץ
    private String email;          // מייל
    private String notes;          // טקסט חופשי
    private String attachmentUrl;  // לינק למסמך / קובץ (Google Drive, PDF וכו')
    private String priority;
    public Task() {}

    // Getters & setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Integer getSubTasksCompleted() { return subTasksCompleted; }

    public void setSubTasksCompleted(Integer subTasksCompleted) { this.subTasksCompleted = subTasksCompleted; }

    public Integer getSubTasksTotal() { return subTasksTotal; }

    public void setSubTasksTotal(Integer subTasksTotal) { this.subTasksTotal = subTasksTotal; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getDueDate() { return dueDate; }

    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getAssignee() { return assignee; }

    public void setAssignee(String assignee) { this.assignee = assignee; }

    public LocalDate getCreatedDate() { return createdDate; }

    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public LocalDate getCompletedDate() { return completedDate; }

    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNavUrl() { return navUrl; }
    public void setNavUrl(String navUrl) { this.navUrl = navUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }


}
