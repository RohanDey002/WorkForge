package com.taskmanagement.aitaskmanagement.controller;

import com.taskmanagement.aitaskmanagement.DTO.request.RegisterEmployeeRequest;
import com.taskmanagement.aitaskmanagement.DTO.request.RegisterManagerRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import com.taskmanagement.aitaskmanagement.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Admin')")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/managers")
    public ResponseEntity<UserResponse> createManager(@Valid @RequestBody RegisterManagerRequest request){

        UserResponse manager = adminService.createManager(request);

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(manager);
    }

    @PostMapping("/employees")
    public ResponseEntity<UserResponse> createEmployee(@Valid @RequestBody RegisterEmployeeRequest request){

        UserResponse employee = adminService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employee);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers(){

        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{userid}")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable Long userid){

        adminService.deleteUser(userid);

        return ResponseEntity.ok("User deleted successfully");
    }

}
