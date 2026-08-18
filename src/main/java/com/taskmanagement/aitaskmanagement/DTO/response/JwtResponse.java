package com.taskmanagement.aitaskmanagement.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse {

    private String token;

    private String type;

    private String email;

    private String role;
}
