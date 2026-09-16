package com.taskmanagement.aitaskmanagement.kafka.producer;


import com.taskmanagement.aitaskmanagement.kafka.event.EmployeeAssignEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeAssignProducer {

    private static final String EMPLOYEE_ASSIGN_TOPIC = "employee-assigned";

    private final KafkaTemplate<String, EmployeeAssignEvent> kafkaTemplate;

    public void publishEmployeeAssign(EmployeeAssignEvent event){
        kafkaTemplate.send(
                EMPLOYEE_ASSIGN_TOPIC,
                String.valueOf(event.getEmployeeId()),
                event
        );
    }
}
