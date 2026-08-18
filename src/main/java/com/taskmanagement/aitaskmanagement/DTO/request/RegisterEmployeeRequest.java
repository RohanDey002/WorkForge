package com.taskmanagement.aitaskmanagement.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterEmployeeRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contains at least 8 digits")
    private  String password;

    @NotBlank(message = "Manager Id is required")
    private Long managerId;
}
