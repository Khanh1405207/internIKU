package com.example.taskmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private Long id;
    @NotNull(message = "Name cannot null")
    private String name;
    @NotNull(message = "Email cannot null")
    private String email;
}
