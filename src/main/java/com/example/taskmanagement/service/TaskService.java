package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.CreateTaskRequest;
import com.example.taskmanagement.dto.request.UpdateTaskRequest;
import com.example.taskmanagement.dto.response.TaskResponse;
import com.example.taskmanagement.entity.Enum.TaskStatus;
import com.example.taskmanagement.entity.Enum.UserStatus;
import com.example.taskmanagement.entity.ProjectEntity;
import com.example.taskmanagement.entity.TaskEntity;
import com.example.taskmanagement.entity.UserEntity;
import com.example.taskmanagement.exception.BadRequestException;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAllWithProjectAndAssignee()
                .stream()
                .map(TaskResponse::new)
                .toList();
    }

    public TaskResponse getTasksById(Long id){
        TaskEntity task=taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task not found")
        );
        return new TaskResponse(task);
    }

    public List<TaskResponse> getTasksByProject(Long projectId){
        return taskRepository.findByProjectEntity_Id(projectId)
                .stream()
                .map(TaskResponse::new)
                .toList();
    }

    public List<TaskResponse> getTasksByAssignee(Long assigneeId){
        return taskRepository.findByAssignee_Id(assigneeId)
                .stream()
                .map(TaskResponse::new)
                .toList();
    }

    public List<TaskResponse> getMyTask(String email){
        return taskRepository.findByAssignee_Email(email)
                .stream()
                .map(TaskResponse::new)
                .toList();
    }

    public TaskResponse createTask(CreateTaskRequest createTaskRequest){
        ProjectEntity project=projectRepository.findById(createTaskRequest.getProjectId()).orElseThrow(
                () -> new ResourceNotFoundException("Project not found")
        );
        if (!createTaskRequest.getDeadline().isAfter(Instant.now())){
            throw new BadRequestException("Deadline must be in the future");
        }
        TaskEntity task=new TaskEntity(
                createTaskRequest.getTitle(),
                createTaskRequest.getDescription(),
                createTaskRequest.getDeadline(),
                project);
        taskRepository.save(task);
        return new TaskResponse(task);
    }

    public TaskResponse updateTask(UpdateTaskRequest updateTaskRequest){
        TaskEntity task=taskRepository.findById(updateTaskRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Task not found")
        );
        if (updateTaskRequest.getDeadline().isBefore(Instant.now())){
            throw new BadRequestException("Deadline must be in the future");
        }
        task.setTitle(updateTaskRequest.getTitle());
        task.setDescription(updateTaskRequest.getDescription());
        task.setDeadline(updateTaskRequest.getDeadline());
        return new TaskResponse(task);
    }

    public TaskResponse assign(Long taskId,Long assigneeId){
        TaskEntity task=taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task not found")
        );
        UserEntity user=userRepository.findById(assigneeId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        if (UserStatus.INACTIVE.equals(user.getStatus())) {
            throw new BadRequestException("Cannot assign task to inactive user");
        }
        if (task.getTaskStatus() != TaskStatus.TODO) {
            throw new BadRequestException("Task must be TODO to assign");
        }
        if (task.isOverdue()){
            throw new BadRequestException("Task is end, cannot assign");
        }
        Long projectId=task.getProjectEntity().getId();
        if(!projectRepository.existsByIdAndUsers_Id(projectId,assigneeId)){
            throw new BadRequestException("User is not same project of task");
        }
        task.assign(user);
        return new TaskResponse(task);
    }

    public TaskResponse startTask(Long taskId){
        TaskEntity task=taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task not found")
        );
        if (task.getAssignee()== null){
            throw new BadRequestException("Task must have assignee to start");
        }
        task.start();
        return new TaskResponse(task);
    }

    public TaskResponse completeTask(Long taskId){
        TaskEntity task=taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task not found")
        );
        if (task.getTaskStatus() != TaskStatus.IN_PROGRESS){
            throw new BadRequestException("Task must start to complete");
        }
        task.complete();
        return new TaskResponse(task);
    }
}
