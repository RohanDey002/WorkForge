package com.taskmanagement.aitaskmanagement.DTO.response;


import com.taskmanagement.aitaskmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class AuthResult {

    private String accessToken;
    private String refreshToken;
    private UserResponse user;
}
