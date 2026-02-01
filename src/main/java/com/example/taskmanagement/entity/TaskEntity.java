package com.example.taskmanagement.entity;

import com.example.taskmanagement.entity.Enum.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "deadline", nullable = false)
    private Instant deadline;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",referencedColumnName = "id",nullable = false)
    private ProjectEntity projectEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id",referencedColumnName = "id")
    private UserEntity assignee;

    public TaskEntity(String title, String description, Instant deadline, ProjectEntity projectEntity) {
        if (title == null|| title.isBlank()){
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (deadline == null|| deadline.isBefore(Instant.now())){
            throw new IllegalArgumentException("Deadline is not valid");
        }
        if (projectEntity == null){
            throw new IllegalArgumentException("Task must be in a Project");
        }
        this.title = title;
        this.description = description;
        this.taskStatus = TaskStatus.TODO;
        this.createdAt= Instant.now();
        this.deadline = deadline;
        this.projectEntity = projectEntity;
    }

    public void assign(UserEntity userEntity){
        if (userEntity == null){
            throw new IllegalArgumentException("Assignee cannot be null");
        }
        if (taskStatus == TaskStatus.DONE){
            throw new IllegalStateException("Cannot assign completed task");
        }
        if (assignee != null){
            throw new IllegalArgumentException("This task already have an assignee");
        }
        this.assignee= userEntity;
    }

    public void start(){
        if (taskStatus == TaskStatus.DONE){
            throw new IllegalStateException("Task done, cannot start again");
        }
        if (taskStatus == TaskStatus.IN_PROGRESS){
            throw new IllegalStateException(("Task already started"));
        }
        if (assignee == null){
            throw new IllegalArgumentException("No assignee, Task cannot start");
        }
        this.taskStatus = TaskStatus.IN_PROGRESS;
    }

    public void complete(){
        if (taskStatus == TaskStatus.DONE){
            throw new IllegalStateException(("Task already done"));
        }
        if (taskStatus != TaskStatus.IN_PROGRESS){
            throw new IllegalStateException(("Task must be IN_PROGRESS to complete"));
        }
        this.taskStatus = TaskStatus.DONE;
    }

    public boolean isOverdue(){
        return deadline.isBefore(Instant.now()) && taskStatus!=TaskStatus.DONE;
    }
}
