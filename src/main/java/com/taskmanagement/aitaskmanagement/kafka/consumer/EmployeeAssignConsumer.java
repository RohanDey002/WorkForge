package com.taskmanagement.aitaskmanagement.kafka.consumer;

import com.taskmanagement.aitaskmanagement.kafka.event.EmployeeAssignEvent;
import com.taskmanagement.aitaskmanagement.notifcation.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeAssignConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "employee-assigned",
            groupId = "task-management-notification-group"
    )
    public void consume(EmployeeAssignEvent event){
        notificationService.createEmployeeAssignNotification(event);
    }
}
