package com.example.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(200,"Success",data);
    }

    public static <T> ApiResponse<T> create(T data){
        return new ApiResponse<>(201, "Created", data);
    }

    public static <T> ApiResponse<T> badRequest(String message){
        return new ApiResponse<>(400,message,null);
    }

    public static <T> ApiResponse<T> conflict(String message){
        return new ApiResponse<>(409,message,null);
    }

    public static <T> ApiResponse<T> notFound(String message){
        return new ApiResponse<>(404,message,null);
    }

    public static <T> ApiResponse<T> unauthorized(String message){
        return new ApiResponse<>(401,message,null);
    }

    public static <T> ApiResponse<T> internalServerError(T data){
        return new ApiResponse<>(500, "Internal Server Error", data);
    }
}
