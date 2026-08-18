package com.taskmanagement.aitaskmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private LocalDate time;
    private int value;
    private String error;
    private String message;
}
