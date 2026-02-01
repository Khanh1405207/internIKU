package com.example.taskmanagement.service;

import com.example.taskmanagement.entity.ProjectEntity;
import com.example.taskmanagement.entity.TaskEntity;
import com.example.taskmanagement.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskService {

    private Map<Long, TaskEntity> taskStore=new HashMap<>();
    private Map<Long, UserEntity> userStore=new HashMap<>();
    private Map<Long, ProjectEntity> projectStore=new HashMap<>();

    private Long taskIdSequency=1L;

    public TaskEntity createTask(String title, String description, Instant deadline, Long projectId){
        TaskEntity taskEntity = new TaskEntity(title,description,deadline,getProjectOrThrow(projectId));
        taskEntity.setId(taskIdSequency++);
        taskStore.put(taskEntity.getId(), taskEntity);
        return taskEntity;
    }

    public TaskEntity updateTask(Long taskId, String title, String description, Instant deadline){
        TaskEntity taskEntity =getTaskOrThrow(taskId);
        taskEntity.setTitle(title);
        taskEntity.setDescription(description);
        taskEntity.setDeadline(deadline);
        return taskEntity;
    }

    public void assignTask(Long userId,Long taskId){
        UserEntity userEntity =getUserOrThrow(userId);
        TaskEntity taskEntity =getTaskOrThrow(taskId);
        taskEntity.assign(userEntity);
    }

    public void startTask(Long taskId){
        TaskEntity taskEntity =getTaskOrThrow(taskId);
        taskEntity.start();
    }

    public void completeTask(Long taskId){
        TaskEntity taskEntity =getTaskOrThrow(taskId);
        taskEntity.complete();
    }

    public void deleteTask(Long taskId){
        TaskEntity taskEntity =getTaskOrThrow(taskId);
        taskStore.remove(taskEntity.getId());
    }

    public TaskEntity getTaskOrThrow(Long taskId){
        TaskEntity taskEntity =taskStore.get(taskId);
        if (taskEntity == null){
            throw new IllegalArgumentException("Task not found");
        }
        return taskEntity;
    }

    public UserEntity getUserOrThrow(Long userId){
        UserEntity userEntity =userStore.get(userId);
        if (userEntity == null){
            throw new IllegalArgumentException("User not found");
        }
        return userEntity;
    }

    public ProjectEntity getProjectOrThrow(Long projectId){
        ProjectEntity projectEntity =projectStore.get(projectId);
        if (projectEntity == null){
            throw new IllegalArgumentException("Project not found");
        }
        return projectEntity;
    }

    public void addUser(UserEntity userEntity){
        userStore.put(userEntity.getId(), userEntity);
    }

    public void addProject(ProjectEntity projectEntity){
        projectStore.put(projectEntity.getId(), projectEntity);
    }

}
