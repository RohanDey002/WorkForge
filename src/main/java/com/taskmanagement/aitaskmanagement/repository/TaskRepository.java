package com.taskmanagement.aitaskmanagement.repository;

import com.taskmanagement.aitaskmanagement.entity.Priority;
import com.taskmanagement.aitaskmanagement.entity.Task;
import com.taskmanagement.aitaskmanagement.entity.TaskStatus;
import com.taskmanagement.aitaskmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByAssignedTo(User employee);

    List<Task> findByAssignedBy(User manager);

    List<Task> findByAssignedToAndStatus(User employee, TaskStatus status);

    List<Task> findByAssignedToAndPriority(User employee, Priority priority);

    List<Task> findByAssignedByAndStatus(User manager, TaskStatus status);

}
