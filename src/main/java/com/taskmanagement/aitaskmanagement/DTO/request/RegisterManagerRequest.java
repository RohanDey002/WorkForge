package com.taskmanagement.aitaskmanagement.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterManagerRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8 , message = "Password should be minimum of 8 digits")
    @NotBlank(message = "Password is required")
    private String password;
}
