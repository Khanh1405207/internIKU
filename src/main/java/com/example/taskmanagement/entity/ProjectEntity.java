package com.example.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "created_at")
    private Instant createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by",referencedColumnName = "id",nullable = false)
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
