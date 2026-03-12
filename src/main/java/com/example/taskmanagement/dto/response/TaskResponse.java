package com.example.taskmanagement.dto.response;

import com.example.taskmanagement.entity.TaskEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Instant deadline;
    private String project;
    private String assignee;

    public TaskResponse(TaskEntity task){
        this.id=task.getId();
        this.title=task.getTitle();
        this.description=task.getDescription();
        this.deadline=task.getDeadline();
        this.project=task.getProjectEntity().getName();
        if (task.getAssignee() == null){
            this.assignee="No one assign this task";
        }else {
            this.assignee=task.getAssignee().getName();
        }
    }
}
