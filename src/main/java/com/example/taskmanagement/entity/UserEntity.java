package com.example.taskmanagement.entity;

import com.example.taskmanagement.entity.Enum.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public UserEntity(String name, String email, String password) {
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

    public void deactivate(){
        if (status == UserStatus.INACTIVE){
            throw new IllegalStateException("User already inactive");
        }
        this.status = UserStatus.INACTIVE;
    }
}
