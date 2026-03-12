package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.ApiResponse;
import com.example.taskmanagement.dto.request.CreateTaskRequest;
import com.example.taskmanagement.dto.request.UpdateTaskRequest;
import com.example.taskmanagement.dto.response.TaskResponse;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(){
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskService.getAllTasks()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable("id") Long id){
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskService.getTasksById(id)));
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByProject(@PathVariable("id") Long id){
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskService.getTasksByProject(id)));
    }

    @GetMapping("/assignee/{id}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByAssignee(@PathVariable("id") Long id){
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskService.getTasksByAssignee(id)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getMyTask(Authentication authentication){
        String email=authentication.getName();
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskService.getMyTask(email)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request){
        TaskResponse taskResponse=taskService.createTask(request);
        return ResponseEntity
                .status(201)
                .body(ApiResponse.create(taskResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@Valid @RequestBody UpdateTaskRequest request,
                                        @PathVariable("id") Long id){
        request.setId(id);
        TaskResponse taskResponse=taskService.updateTask(request);
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskResponse));
    }

    @PatchMapping("/{taskId}/assign/{assigneeId}")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTask(@PathVariable("taskId") Long taskId,
                                        @PathVariable("assigneeId") Long assigneeId){
        TaskResponse taskResponse=taskService.assign(taskId,assigneeId);
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskResponse));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<ApiResponse<TaskResponse>> startTask(@PathVariable("id") Long id){
        TaskResponse taskResponse=taskService.startTask(id);
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskResponse));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TaskResponse>> completeTask(@PathVariable("id") Long id){
        TaskResponse taskResponse=taskService.completeTask(id);
        return ResponseEntity
                .status(200)
                .body(ApiResponse.success(taskResponse));
    }
}
