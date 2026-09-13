package com.taskmanagement.aitaskmanagement.kafka.consumer;

import com.taskmanagement.aitaskmanagement.kafka.event.TaskAssignEvent;
import com.taskmanagement.aitaskmanagement.notifcation.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskAssignConsumer {

    private final NotificationService notificationService;

            @KafkaListener(
                    topics = "task-assigned",
                    groupId = "task-management-notification-group"
            )
    public void consume(TaskAssignEvent event){

                notificationService.createTaskAssignNotification(event);
            }
}
