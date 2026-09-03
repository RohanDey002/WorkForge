package com.taskmanagement.aitaskmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception){

        ErrorResponse response = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException exception){

        ErrorResponse response = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException exception){

        ErrorResponse response = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public  ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException exception){

        ErrorResponse response = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<ErrorResponse> handleManyRequest(TooManyListenersException exception){
        ErrorResponse response = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Request",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(response);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception){

        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error->
                        errors.put(error.getField(),error.getDefaultMessage()));

        ErrorResponse response = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public  ResponseEntity<ErrorResponse>genericException(Exception exception){

        ErrorResponse response = new ErrorResponse(
                LocalDate.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Something went wrong"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
