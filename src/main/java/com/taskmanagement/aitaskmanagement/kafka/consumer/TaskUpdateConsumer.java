package com.taskmanagement.aitaskmanagement.kafka.consumer;

import com.taskmanagement.aitaskmanagement.kafka.event.TaskUpdateEvent;
import com.taskmanagement.aitaskmanagement.notifcation.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskUpdateConsumer {
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "task-updated",
            groupId = "task-management-notification-group"
    )
    public void consume(TaskUpdateEvent event){

        notificationService.createTaskUpdateNotification(event);
    }

}
