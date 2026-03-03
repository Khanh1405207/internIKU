package com.example.taskmanagement.dto.request;

import com.example.taskmanagement.entity.Enum.TaskStatus;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Deadline cannot be null")
    @Future(message = "Deadline must be a future date")
    private Instant deadline;

    @NotNull(message = "Project cannot be null")
    private Long projectId;

    private Long assigneeId;
}
