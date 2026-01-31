package com.example.taskmanagement.model;

import com.example.taskmanagement.model.Enum.UserStatus;

import java.time.Instant;

public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Instant createdAt;
    private UserStatus status;

    public User(String name,String email,String password){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be Blank");
        }
    }
}
