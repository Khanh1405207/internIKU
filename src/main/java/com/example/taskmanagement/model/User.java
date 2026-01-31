package com.example.taskmanagement.model;

import com.example.taskmanagement.model.Enum.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Instant createdAt;
    private UserStatus status;

    public User(String name, String email, String password) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("Email cannot be blank");
        }
        if (!email.contains("@")){
            throw new IllegalArgumentException("Email format invalid");
        }
        if (password == null || password.length() <6){
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
        this.status=UserStatus.ACTIVE;
    }

    public void deActivate(){
        if (status == UserStatus.DISABLED){
            throw new IllegalStateException("User already disabled");
        }
        this.status = UserStatus.DISABLED;
    }
}
