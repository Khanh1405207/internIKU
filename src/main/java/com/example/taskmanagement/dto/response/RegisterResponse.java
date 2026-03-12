package com.example.taskmanagement.dto.response;

import com.example.taskmanagement.entity.RoleEntity;
import com.example.taskmanagement.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;

    public RegisterResponse(UserEntity user){
        this.id=user.getId();
        this.name=user.getName();
        this.email=user.getEmail();
        this.roles=user.getRoles()
                .stream()
                .map(RoleEntity::getRoleName)
                .collect(Collectors.toSet());
    }
}
