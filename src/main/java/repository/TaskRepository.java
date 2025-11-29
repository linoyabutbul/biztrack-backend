package com.example.biztrack.repository;

import com.example.biztrack.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Tasks for today:
    // show tasks that are not completed yet OR completed today.
    // Tasks completed in previous days will not appear.
    List<Task> findByCompletedDateIsNullOrCompletedDate(LocalDate completedDate);
}
