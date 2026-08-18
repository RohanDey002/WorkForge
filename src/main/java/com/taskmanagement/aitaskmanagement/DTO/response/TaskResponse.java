package com.taskmanagement.aitaskmanagement.DTO.response;


import com.taskmanagement.aitaskmanagement.entity.Priority;
import com.taskmanagement.aitaskmanagement.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TaskResponse {
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private Priority priority;

    private LocalDate dueDate;

    private Long assignedById;

    private String assignedByName;

    private Long assignedToId;

    private String assignedToName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
