package com.taskmanagement.aitaskmanagement.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTaskStatus {

    @NotBlank(message = "Task status is required")
     private String status;
}
