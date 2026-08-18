package com.taskmanagement.aitaskmanagement.DTO.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateTaskStatus {

    @NotBlank(message = "Task status is required")
     private String status;
}
