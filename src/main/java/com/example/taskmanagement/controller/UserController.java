package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.ApiResponse;
import com.example.taskmanagement.dto.request.UpdateUserRequest;
import com.example.taskmanagement.dto.request.CreateUserRequest;
import com.example.taskmanagement.dto.response.UserResponse;
import com.example.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(userService.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request){
        UserResponse userResponse=userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.create(userResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@Valid @RequestBody UpdateUserRequest request,
                                        @PathVariable("id") Long id){
        request.setId(id);
        UserResponse userResponse=userService.updateUser(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(userResponse));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ApiResponse<UserResponse>> disableUser(@PathVariable("id") Long id){
        UserResponse userResponse=userService.disableUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(userResponse));
    }
}
