package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.UpdateUserRequest;
import com.example.taskmanagement.dto.request.CreateUserRequest;
import com.example.taskmanagement.dto.response.UserResponse;
import com.example.taskmanagement.entity.Enum.UserStatus;
import com.example.taskmanagement.entity.UserEntity;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findByStatus(UserStatus.ACTIVE)
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    public UserResponse getUserById(Long id){
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        return new UserResponse(userEntity);
    }

    public void createUser(CreateUserRequest createUserRequest){
        if (userRepository.existsByEmail(createUserRequest.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        UserEntity userEntity =new UserEntity(
                createUserRequest.getName(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword());
        userRepository.save(userEntity);
    }

    public void updateUser(UpdateUserRequest updateUserRequest){
        UserEntity userEntity = userRepository.findById(updateUserRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        if (userRepository.existsByEmailAndIdNot(updateUserRequest.getEmail(), updateUserRequest.getId())){
            throw new IllegalArgumentException("Email already exists");
        }
        userEntity.setName(updateUserRequest.getName());
        userEntity.setEmail(updateUserRequest.getEmail());
    }

    public void disableUser(Long id){
        UserEntity userEntity =userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        userEntity.deactivate();
    }
}
