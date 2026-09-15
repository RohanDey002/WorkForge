package com.taskmanagement.aitaskmanagement.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateEvent {

    private Long taskId;
    private String taskTitle;
    private Long employeeId;
    private String employeeName;
    private Long managerId;
    private String managerName;
    private String oldStatus;
    private String newStatus;
}
