package com.example.taskmanagement.dto.response;

import com.example.taskmanagement.entity.ProjectEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private String name;
    private String description;
    private String createdBy;

    public ProjectResponse(ProjectEntity project){
        this.name=project.getName();
        this.description=project.getDescription();
        this.createdBy=project.getCreatedBy().getName();
    }
}
