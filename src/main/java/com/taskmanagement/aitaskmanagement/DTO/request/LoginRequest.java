package com.taskmanagement.aitaskmanagement.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Please Provide Valid Email")
    @NotBlank(message = " Email Is Required")
     private String email;

    @NotBlank(message = "Password Is Required")
     private String password;
}
