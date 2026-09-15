package com.taskmanagement.aitaskmanagement.kafka.producer;

import com.taskmanagement.aitaskmanagement.kafka.event.TaskUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskUpdateProducer {

    private static final String  TASK_UPDATE_TOPIC = "task-updated";

    private  final KafkaTemplate<String , TaskUpdateEvent> kafkaTemplate;

    public void publishTaskUpdated(TaskUpdateEvent event){

        kafkaTemplate.send(
                TASK_UPDATE_TOPIC,
                String.valueOf(event.getTaskId()),
                event
        );
    }
}
