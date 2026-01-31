package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskService {

    private Map<Long,Task> taskStore=new HashMap<>();
    private Map<Long,User> userStore=new HashMap<>();
    private Map<Long,Project> projectStore=new HashMap<>();

    private Long taskIdSequency=1L;

    public Task createTask(String title, String description, Instant deadline, Long projectId){
        Task task= new Task(title,description,deadline,getProjectOrThrow(projectId));
        task.setId(taskIdSequency++);
        taskStore.put(task.getId(),task);
        return task;
    }

    public Task updateTask(Long taskId,String title, String description, Instant deadline){
        Task task=getTaskOrThrow(taskId);
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        return task;
    }

    public void assignTask(Long userId,Long taskId){
        User user=getUserOrThrow(userId);
        Task task=getTaskOrThrow(taskId);
        task.assign(user);
    }

    public void startTask(Long taskId){
        Task task=getTaskOrThrow(taskId);
        task.start();
    }

    public void completeTask(Long taskId){
        Task task=getTaskOrThrow(taskId);
        task.complete();
    }

    public void deleteTask(Long taskId){
        Task task=getTaskOrThrow(taskId);
        taskStore.remove(task.getId());
    }

    public Task getTaskOrThrow(Long taskId){
        Task task=taskStore.get(taskId);
        if (task == null){
            throw new IllegalArgumentException("Task not found");
        }
        return task;
    }

    public User getUserOrThrow(Long userId){
        User user=userStore.get(userId);
        if (user == null){
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public Project getProjectOrThrow(Long projectId){
        Project project=projectStore.get(projectId);
        if (project == null){
            throw new IllegalArgumentException("Project not found");
        }
        return project;
    }

    public void addUser(User user){
        userStore.put(user.getId(),user);
    }

    public void addProject(Project project){
        projectStore.put(project.getId(),project);
    }

}
