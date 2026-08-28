package com.taskmanagement.aitaskmanagement.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {

    private  String message;

    private  UserResponse user;
}
