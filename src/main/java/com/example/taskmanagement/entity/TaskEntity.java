package com.example.taskmanagement.entity;

import com.example.taskmanagement.entity.Enum.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TaskEntity {
    private Long id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private Instant createdAt;
    private Instant deadline;
    private ProjectEntity projectEntity;
    private UserEntity assignee;

    public TaskEntity(String title, String description, Instant deadline, ProjectEntity projectEntity) {
        if (title == null|| title.isBlank()){
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (description == null|| description.isBlank()){
            throw new IllegalArgumentException("Description cannot be blank");
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
