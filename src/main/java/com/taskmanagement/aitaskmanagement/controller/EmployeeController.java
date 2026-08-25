package com.taskmanagement.aitaskmanagement.controller;

import com.taskmanagement.aitaskmanagement.DTO.request.TaskRequest;
import com.taskmanagement.aitaskmanagement.DTO.request.UpdateTaskStatus;
import com.taskmanagement.aitaskmanagement.DTO.response.TaskResponse;
import com.taskmanagement.aitaskmanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/employees")
@PreAuthorize("hasRole('Employee')")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getMyTasks(){

        return ResponseEntity.ok(employeeService.getMyTasks());
    }

    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTasks(
            @PathVariable Long taskId ,
            @Valid @RequestBody UpdateTaskStatus request
            ){

        TaskResponse taskResponse = employeeService.updateTask(taskId, request.getStatus());

        return ResponseEntity.ok(taskResponse);
    }
}
