package com.example.taskmanagement.exception;

import com.example.taskmanagement.dto.ApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        HttpStatus status=HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(new ApiResponse<>(400,"Validation error",errors));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException e){

        HttpStatus status=HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.notFound(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException e){

        HttpStatus status=HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.badRequest(e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException e){

        HttpStatus status=HttpStatus.CONFLICT;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.conflict(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(500, "Internal server error", null));
    }

}
