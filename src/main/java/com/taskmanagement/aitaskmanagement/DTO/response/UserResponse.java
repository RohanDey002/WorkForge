package com.taskmanagement.aitaskmanagement.DTO.response;

import com.taskmanagement.aitaskmanagement.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Role role;

    private Long managerId;

    private String presence;

}
