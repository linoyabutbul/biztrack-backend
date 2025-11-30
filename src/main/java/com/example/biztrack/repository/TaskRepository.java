package com.example.biztrack.repository;
import java.time.LocalDate;
import java.util.List;
import com.example.biztrack.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Tasks for today:
    // show tasks that are not completed yet OR completed today.
    // Tasks completed in previous days will not appear.
    List<Task> findByCompletedDateIsNullOrCompletedDate(LocalDate completedDate);
}
