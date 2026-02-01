package com.example.taskmanagement.dto.request;

import com.example.taskmanagement.entity.ProjectEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {

    private Long id;
    @NotNull(message = "Name cannot null")
    private String name;
    private String description;

}
