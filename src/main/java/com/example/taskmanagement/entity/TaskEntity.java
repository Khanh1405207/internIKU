package com.example.taskmanagement.entity;

import com.example.taskmanagement.entity.Enum.TaskStatus;
import com.example.taskmanagement.exception.BadRequestException;
import com.example.taskmanagement.exception.ConflictException;
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
        this.title = title;
        this.description = description;
        this.taskStatus = TaskStatus.TODO;
        this.createdAt= Instant.now();
        this.deadline = deadline;
        this.projectEntity = projectEntity;
    }

    public void assign(UserEntity userEntity){
        if (userEntity == null){
            throw new BadRequestException("Assignee cannot be null");
        }
        if (this.taskStatus != TaskStatus.TODO){
            throw new BadRequestException("Task must be TODO to assign");
        }
        if (this.assignee != null){
            throw new ConflictException("This task already have an assignee");
        }
        this.assignee= userEntity;
    }

    public void start(){
        if (this.taskStatus == TaskStatus.DONE){
            throw new BadRequestException("Task done, cannot start again");
        }
        if (this.taskStatus == TaskStatus.IN_PROGRESS){
            throw new ConflictException(("Task already started"));
        }
        if (this.assignee == null){
            throw new BadRequestException("No assignee, Task cannot start");
        }
        this.taskStatus = TaskStatus.IN_PROGRESS;
    }

    public void complete(){
        if (this.taskStatus == TaskStatus.DONE){
            throw new ConflictException(("Task already done"));
        }
        if (this.taskStatus != TaskStatus.IN_PROGRESS){
            throw new BadRequestException(("Task must be IN_PROGRESS to complete"));
        }
        this.taskStatus = TaskStatus.DONE;
    }

    public boolean isOverdue(){
        return deadline.isBefore(Instant.now()) && taskStatus!=TaskStatus.DONE;
    }
}
