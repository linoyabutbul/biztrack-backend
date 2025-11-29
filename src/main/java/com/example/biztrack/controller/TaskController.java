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

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Get today's checklist
    @GetMapping("/today")
    public List<Task> getTodayTasks() {
        LocalDate today = LocalDate.now();
        return taskRepository.findByCompletedDateIsNullOrCompletedDate(today);
    }

    // Get all (for debug)
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Create new task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        task.setId(null);
        task.setStatus(task.getStatus() == null ? "TODO" : task.getStatus());
        task.setCreatedDate(LocalDate.now());
        if (task.getSubTasksCompleted() == null) {
            task.setSubTasksCompleted(0);
        }
        if (task.getPriority() == null || task.getPriority().isBlank()) {
            task.setPriority("NORMAL"); // ברירת מחדל
        }
        return taskRepository.save(task);
    }

    // Update entire task (for editing)
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updated) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
            if ("DONE".equalsIgnoreCase(updated.getStatus())) {
                existing.setCompletedDate(LocalDate.now());
            }
        }
        if (updated.getSubTasksCompleted() != null) {
            existing.setSubTasksCompleted(updated.getSubTasksCompleted());
        }
        if (updated.getSubTasksTotal() != null) {
            existing.setSubTasksTotal(updated.getSubTasksTotal());
        }
        if (updated.getStartDate() != null) existing.setStartDate(updated.getStartDate());
        if (updated.getDueDate() != null) existing.setDueDate(updated.getDueDate());
        if (updated.getAssignee() != null) existing.setAssignee(updated.getAssignee());
        if (updated.getAddress() != null) existing.setAddress(updated.getAddress());
        if (updated.getNavUrl() != null) existing.setNavUrl(updated.getNavUrl());
        if (updated.getPhone() != null) existing.setPhone(updated.getPhone());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
        if (updated.getNotes() != null) existing.setNotes(updated.getNotes());
        if (updated.getAttachmentUrl() != null) existing.setAttachmentUrl(updated.getAttachmentUrl());
        if (updated.getPriority() != null) existing.setPriority(updated.getPriority());

        return taskRepository.save(existing);
    }

    // Delete task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }




}
