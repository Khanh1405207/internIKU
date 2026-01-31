package com.example.taskmanagement.model;

import com.example.taskmanagement.model.Enum.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private Instant deadline;
    private Project project;
    private User assignee;

    public Task(String title, String description, Instant deadline, Project project) {
        if (title == null|| title.isBlank()){
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (description == null|| description.isBlank()){
            throw new IllegalArgumentException("Description cannot be blank");
        }
        if (deadline == null|| deadline.isBefore(Instant.now())){
            throw new IllegalArgumentException("Deadline is not valid");
        }
        if (project == null){
            throw new IllegalArgumentException("Task must be in a Project");
        }
        this.title = title;
        this.description = description;
        this.taskStatus = TaskStatus.TODO;
        this.deadline = deadline;
        this.project = project;
    }

    public void assign(User user){
        if (user == null){
            throw new IllegalArgumentException("Assignee cannot be null");
        }
        if (taskStatus == TaskStatus.DONE){
            throw new IllegalStateException("Cannot assign completed task");
        }
        if (assignee != null){
            throw new IllegalArgumentException("This task already have an assignee");
        }
        this.assignee= user;
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
