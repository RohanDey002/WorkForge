package com.taskmanagement.aitaskmanagement.notifcation.service;


import com.taskmanagement.aitaskmanagement.kafka.event.TaskAssignEvent;
import com.taskmanagement.aitaskmanagement.kafka.event.TaskUpdateEvent;
import com.taskmanagement.aitaskmanagement.notifcation.entity.Notification;
import com.taskmanagement.aitaskmanagement.notifcation.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createTaskAssignNotification(TaskAssignEvent event){



        String message = "You have been assigned a new task"
                +" by "
                +event.getAssignedByName();

        Notification notification = Notification.builder()
                .recipientId(event.getAssignedToId())
                .message(message)
                .build();

         notificationRepository.save(notification);


    }

    public void createTaskUpdateNotification(TaskUpdateEvent event){

        String message = event.getEmployeeName()
                +" updated task status from "
                +event.getOldStatus()
                +" to "
                +event.getNewStatus();


        Notification notification = Notification.builder()
                .recipientId(event.getManagerId())
                .message(message)
                .build();

        notificationRepository.save(notification);
    }
}


