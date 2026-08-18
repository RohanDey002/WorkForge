package com.taskmanagement.aitaskmanagement.service;

import com.taskmanagement.aitaskmanagement.DTO.request.TaskRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.TaskResponse;
import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import com.taskmanagement.aitaskmanagement.entity.Role;
import com.taskmanagement.aitaskmanagement.entity.Task;
import com.taskmanagement.aitaskmanagement.entity.TaskStatus;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.exception.ForbiddenException;
import com.taskmanagement.aitaskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.aitaskmanagement.repository.TaskRepository;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import com.taskmanagement.aitaskmanagement.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskResponse assignTask(TaskRequest request){
        User manager = getCurretUser();

        if(manager.getRole()!= Role.Manager){
            throw new ForbiddenException("Only managers can assign tasks");
        }

        User employee = userRepository.findByIdAndRole(request.getEmployeeId(),Role.Employee)
                .orElseThrow(()->
                        new ResourceNotFoundException("Employee not found with id:"+request.getEmployeeId()));

        if(employee.getManager()==null||
                !employee.getManager()
                        .getId()
                        .equals(manager.getId())
        ){
            throw new ForbiddenException("You cannot assign a task to another manager's employee");
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(TaskStatus.TODO)
                .dueDate(request.getDueDate())
                .assignedBy(manager)
                .assignedTo(employee)
                .build();

        Task savedtask = taskRepository.save(task);

        return mapToTaskResponse(savedtask);

    }

    public List<TaskResponse> getAssignTasks(){

        User manager = getCurretUser();

        return taskRepository.findByAssignedBy(manager)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }

    public List<UserResponse> getEmployees(){
        User manager = getCurretUser();
        return userRepository.findByManager(manager)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    private User getCurretUser(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails)  authentication.getPrincipal();

        return  userDetails.getUser();
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

    private UserResponse mapToUserResponse(User user){
        Long managerId = user.getManager()!=null? user.getManager().getId():null;

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .managerId(managerId)
                .build();
    }
}
