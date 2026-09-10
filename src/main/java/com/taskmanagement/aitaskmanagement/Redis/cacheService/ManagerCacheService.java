package com.taskmanagement.aitaskmanagement.Redis.cacheService;


import com.taskmanagement.aitaskmanagement.DTO.response.TaskResponse;
import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import com.taskmanagement.aitaskmanagement.Redis.Presence.PresenceServices;
import com.taskmanagement.aitaskmanagement.entity.Task;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.repository.TaskRepository;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerCacheService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    @Cacheable(
            cacheNames = "managerEmployees",
            key = "#managerId"
    )
    public List<UserResponse> getEmployees(Long managerId){

        System.out.println(
                "REDIS CACHE MISS -> manager employees"
        );


        return userRepository.findByManagerId(managerId)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Cacheable(
            cacheNames = "managerTasks",
            key = "#managerId"
    )
    public List<TaskResponse> getTasks(Long managerId){
        System.out.println(
                "REDIS CACHE MISS -> manager tasks"
        );

       ;
        return taskRepository.findByAssignedById(managerId)
                .stream()
                .map(this::mapToTaskResponse)
                .toList();
    }


    @CacheEvict(
            cacheNames = "managerEmployees",
            key = "#managerId"
    )
    public void evictEmployees(Long managerId){
        System.out.println(
                "CACHE EVICT----> managerEmployees"
        );
    }

    @CacheEvict(
            cacheNames = "managerTasks",
            key = "#managerId"

    )
    public void evictTasks(Long managerId){
        System.out.println(
                "CACHE EVICT---> managerTasks"
        );
    }

    private TaskResponse mapToTaskResponse(
            Task task
    ) {

        return TaskResponse.builder()

                .id(task.getId())

                .title(task.getTitle())

                .description(task.getDescription())

                .priority(task.getPriority())

                .status(task.getStatus())

                .dueDate(task.getDueDate())

                .assignedById(
                        task.getAssignedBy().getId()
                )

                .assignedByName(
                        task.getAssignedBy().getName()
                )

                .assignedToId(
                        task.getAssignedTo().getId()
                )

                .assignedToName(
                        task.getAssignedTo().getName()
                )

                .createdAt(
                        task.getCreateAt()
                )

                .updatedAt(
                        task.getUpdateAt()
                )

                .build();
    }


    private UserResponse mapToUserResponse(
            User user
    ) {

        Long managerId =
                user.getManager() != null
                        ? user.getManager().getId()
                        : null;




        return UserResponse.builder()

                .id(user.getId())

                .name(user.getName())

                .email(user.getEmail())

                .role(user.getRole())

                .managerId(managerId)

                .build();
    }

}
