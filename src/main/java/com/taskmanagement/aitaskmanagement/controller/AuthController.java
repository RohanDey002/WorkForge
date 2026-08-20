package com.taskmanagement.aitaskmanagement.controller;

import com.taskmanagement.aitaskmanagement.DTO.request.LoginRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.JwtResponse;
import com.taskmanagement.aitaskmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request){

        JwtResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
