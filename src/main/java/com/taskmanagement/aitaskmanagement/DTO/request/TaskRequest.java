package com.taskmanagement.aitaskmanagement.DTO.request;

import com.taskmanagement.aitaskmanagement.entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be past")
    private LocalDate dueDate;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;
}
