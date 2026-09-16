package com.taskmanagement.aitaskmanagement.kafka.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAssignEvent {

    private Long employeeId;
    private String employeeName;
    private Long managerId;
    private String managerName;
}
