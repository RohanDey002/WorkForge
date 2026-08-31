package com.taskmanagement.aitaskmanagement.service;

import com.taskmanagement.aitaskmanagement.DTO.response.TaskResponse;
import com.taskmanagement.aitaskmanagement.Redis.cacheService.EmployeeCacheService;
import com.taskmanagement.aitaskmanagement.Redis.cacheService.ManagerCacheService;
import com.taskmanagement.aitaskmanagement.entity.Task;
import com.taskmanagement.aitaskmanagement.entity.TaskStatus;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.exception.ForbiddenException;
import com.taskmanagement.aitaskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.aitaskmanagement.repository.TaskRepository;
import com.taskmanagement.aitaskmanagement.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final TaskRepository taskRepository;
    private final EmployeeCacheService employeeCacheService;
    private final ManagerCacheService managerCacheService;

    public List<TaskResponse> getMyTasks(){

        User employee = getCurrentUser();

        return employeeCacheService.getEmployeeTasks(employee.getId());
    }

    public TaskResponse updateTask(Long taskid, String status){

        User employee = getCurrentUser();

        Task task = taskRepository.findById(taskid)
                .orElseThrow(()->
                        new ResourceNotFoundException("Task not found with id:"+taskid));

        if(!task.getAssignedTo()
                .getId()
                .equals(employee.getId())){
            throw  new ForbiddenException("You cannot update other's tasks");
        }

        TaskStatus taskStatus;

        try {
            taskStatus =TaskStatus.valueOf(
                    status.toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Invalid task status:"+status);
        }
        task.setStatus(taskStatus);

        Task updatedTask = taskRepository.save(task);

        employeeCacheService.evictEmployeeTask(employee.getId());

        managerCacheService.evictTasks(employee.getManager().getId());


        return mapToTaskResponse(updatedTask);

    }


    private User getCurrentUser(){

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
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
