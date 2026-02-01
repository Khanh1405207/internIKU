package com.example.taskmanagement.repository;

import com.example.taskmanagement.entity.Enum.UserStatus;
import com.example.taskmanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    List<UserEntity> findByStatus(UserStatus status);
}
