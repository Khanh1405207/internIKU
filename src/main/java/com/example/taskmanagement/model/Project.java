package com.example.taskmanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Project {
    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
    private User createdBy;

    public Project(String name, String description, User createdBy) {
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
