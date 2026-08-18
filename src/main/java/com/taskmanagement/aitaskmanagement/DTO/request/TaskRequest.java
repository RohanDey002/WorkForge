package com.taskmanagement.aitaskmanagement.DTO.request;

import com.taskmanagement.aitaskmanagement.entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotBlank(message = "Priority is required")
    private Priority priority;

    @NotBlank(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be past")
    private LocalDate dueDate;

    @NotBlank(message = "Employee ID is required")
    private Long employeeId;
}
