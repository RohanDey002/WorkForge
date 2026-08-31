package com.taskmanagement.aitaskmanagement.Redis.cacheService;


import com.taskmanagement.aitaskmanagement.DTO.response.TaskResponse;
import com.taskmanagement.aitaskmanagement.entity.Task;
import com.taskmanagement.aitaskmanagement.repository.TaskRepository;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeCacheService {

    private final TaskRepository taskRepository;

    @Cacheable(
            cacheNames = "employeeTasks",
            key = "#employeeId"
    )
    public List<TaskResponse>  getEmployeeTasks(Long employeeId){
        System.out.println(
                "REDIS CACHE MISS -> DATABASE"
        );

        return taskRepository.findByAssignedToId(employeeId)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    @CacheEvict(
            cacheNames = "employeeTasks",
            key = "#employeeId"
    )
    public void evictEmployeeTask(Long employeeId){
        System.out.println(
                "REDIS CACHE EVICT -> employeeTasks:"
                        + employeeId);

    }

    private TaskResponse mapToTaskResponse(Task task){


        return TaskResponse.builder()
                .id(task.getId())

                .title(task.getTitle())

                .description(task.getDescription())

                .priority(task.getPriority())

                .status(task.getStatus())

                .dueDate(task.getDueDate())

                .assignedById(task.getAssignedBy().getId())

                .assignedByName(task.getAssignedBy().getName())

                .assignedToId(task.getAssignedTo().getId())

                .assignedToName(task.getAssignedTo().getName())

                .createdAt(task.getCreateAt())

                .updatedAt(task.getUpdateAt())

                .build();
    }


}
