package com.example.taskmanagement.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ProjectEntity {
    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
    private UserEntity createdBy;

    public ProjectEntity(String name, String description, UserEntity createdBy) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Project name cannot be blank");
        }
        if (createdBy == null){
            throw new IllegalArgumentException("CreateBy cannot be blank");
        }
        this.name = name;
        this.description = description;
        this.createdAt= Instant.now();
        this.createdBy = createdBy;
    }
}
