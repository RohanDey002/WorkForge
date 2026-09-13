package com.taskmanagement.aitaskmanagement.kafka.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignEvent {

    private Long taskId;

    private String taskTitle;

    private Long assignedById;

    private String assignedByName;

    private Long assignedToId;

    private String assignedToName;


}
