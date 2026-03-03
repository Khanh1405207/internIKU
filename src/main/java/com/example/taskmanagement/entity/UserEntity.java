package com.example.taskmanagement.entity;

import com.example.taskmanagement.entity.Enum.UserStatus;
import com.example.taskmanagement.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

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

    @ManyToMany(mappedBy = "users")
    private Set<ProjectEntity> projects;

    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
        this.status=UserStatus.ACTIVE;
    }

    public void deactivate(){
        if (this.status == UserStatus.INACTIVE){
            throw new BadRequestException("User already inactive");
        }
        this.status = UserStatus.INACTIVE;
    }
}
