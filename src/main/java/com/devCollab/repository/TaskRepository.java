package com.devCollab.repository;

import com.devCollab.entity.Task;
import com.devCollab.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
    List<Task> findByAssigneeId(Long assigneeId);
}
