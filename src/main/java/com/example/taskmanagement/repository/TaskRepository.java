package com.example.taskmanagement.repository;

import com.example.taskmanagement.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
    List<TaskEntity> findByProjectEntity_Id(Long projectEntityId);

    List<TaskEntity> findByAssignee_Id(Long assigneeId);

    List<TaskEntity> findByAssignee_Email(String assigneeEmail);

    @Query("""
       SELECT t FROM TaskEntity t
       LEFT JOIN FETCH t.projectEntity
       LEFT JOIN FETCH t.assignee
       """)
    List<TaskEntity> findAllWithProjectAndAssignee();
}
