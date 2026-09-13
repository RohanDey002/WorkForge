package com.taskmanagement.aitaskmanagement.kafka.producer;


import com.taskmanagement.aitaskmanagement.kafka.event.TaskAssignEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskEventProducer {

    private static final String TASK_ASSIGN_TOPIC = "task-assigned";
    private final KafkaTemplate<String, TaskAssignEvent> kafkaTemplate;

    public void publishTaskAssigned(TaskAssignEvent event){



        kafkaTemplate.send(
                TASK_ASSIGN_TOPIC,
                String.valueOf(event.getTaskId()),
                event
        );
    }

}
