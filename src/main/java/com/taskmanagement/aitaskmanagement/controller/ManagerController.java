package com.taskmanagement.aitaskmanagement.controller;

import com.taskmanagement.aitaskmanagement.DTO.request.TaskRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.TaskResponse;
import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import com.taskmanagement.aitaskmanagement.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/manager")
@PreAuthorize("has.Role('Manager')")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/employees")
    public ResponseEntity<List<UserResponse>> getEmployees(){

        return ResponseEntity.ok(managerService.getEmployees());
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> assignTask(@Valid @RequestBody TaskRequest request){

        TaskResponse task = managerService.assignTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getAssignTasks(){

        return ResponseEntity.ok(managerService.getAssignTasks());
    }
}
