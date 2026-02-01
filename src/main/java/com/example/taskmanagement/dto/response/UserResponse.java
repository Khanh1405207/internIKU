package com.example.taskmanagement.dto.response;

import com.example.taskmanagement.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String email;

    public UserResponse(UserEntity userEntity){
        this.name= userEntity.getName();
        this.email= userEntity.getEmail();
    }
}
