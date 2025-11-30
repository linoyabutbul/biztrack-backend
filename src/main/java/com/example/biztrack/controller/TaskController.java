package com.example.biztrack.controller;

import com.example.biztrack.model.Task;
import com.example.biztrack.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    // Constructor injection – הכי תקין! ↓↓↓
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/today")
    public List<Task> getTodayTasks() {
        LocalDate today = LocalDate.now();
        return taskRepository.findByCompletedDateIsNullOrCompletedDate(today);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updated) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (updated.getTitle() != null) t.setTitle(updated.getTitle());
        if (updated.getStatus() != null) t.setStatus(updated.getStatus());
        if (updated.getSubTasksCompleted() != null) t.setSubTasksCompleted(updated.getSubTasksCompleted());
        if (updated.getSubTasksTotal() != null) t.setSubTasksTotal(updated.getSubTasksTotal());
        if (updated.getStartDate() != null) t.setStartDate(updated.getStartDate());
        if (updated.getDueDate() != null) t.setDueDate(updated.getDueDate());
        if (updated.getPriority() != null) t.setPriority(updated.getPriority());
        if (updated.getAssignee() != null) t.setAssignee(updated.getAssignee());
        if (updated.getAddress() != null) t.setAddress(updated.getAddress());
        if (updated.getEmail() != null) t.setEmail(updated.getEmail());
        if (updated.getPhone() != null) t.setPhone(updated.getPhone());
        if (updated.getNotes() != null) t.setNotes(updated.getNotes());
        if (updated.getAttachmentUrl() != null) t.setAttachmentUrl(updated.getAttachmentUrl());

        return taskRepository.save(t);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
