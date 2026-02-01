package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.request.UpdateUserRequest;
import com.example.taskmanagement.dto.request.CreateUserRequest;
import com.example.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request){
        userService.createUser(request);
        return ResponseEntity.status(201).body("Create user successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest request,
                                        @PathVariable("id") Long id){
        request.setId(id);
        userService.updateUser(request);
        return ResponseEntity.ok("Update user successfully");
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable("id") Long id){
        userService.disableUser(id);
        return ResponseEntity.ok("Disable user successfully");
    }
}
