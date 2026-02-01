package com.example.taskmanagement.dto.request;

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
public class CreateTaskRequest {

    @NotNull(message = "Title cannot null")
    private String title;
    private String description;
    @NotNull(message = "Deadline cannot null")
    private Instant deadline;
    @NotNull(message = "Project cannot null")
    private Long project;
}
