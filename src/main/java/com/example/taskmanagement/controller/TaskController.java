package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.ApiResponse;
import com.example.taskmanagement.dto.request.CreateTaskRequest;
import com.example.taskmanagement.dto.request.UpdateTaskRequest;
import com.example.taskmanagement.dto.response.TaskResponse;
import com.example.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task API", description = "Task management endpoints")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Return all tasks")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tasks fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskService.getAllTasks()));
    }

    @Operation(summary = "Get task by id", description = "Return task details by task id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskService.getTasksById(id)));
    }

    @Operation(summary = "Get tasks by project", description = "Return all tasks in a project")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tasks fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/projects/{id}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByProject(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskService.getTasksByProject(id)));
    }

    @Operation(summary = "Get tasks by assignee", description = "Return all tasks assigned to a user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tasks fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/assignee/{id}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByAssignee(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskService.getTasksByAssignee(id)));
    }

    @Operation(summary = "Get my tasks", description = "Return tasks assigned to current authenticated user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tasks fetched successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getMyTask(Authentication authentication){
        String email=authentication.getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskService.getMyTask(email)));
    }

    @Operation(summary = "Create task", description = "Create a new task in a project")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Task created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or invalid deadline"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request){
        TaskResponse taskResponse=taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.create(taskResponse));
    }

    @Operation(summary = "Update task", description = "Update task details by id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error or invalid deadline"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@Valid @RequestBody UpdateTaskRequest request,
                                        @PathVariable("id") Long id){
        request.setId(id);
        TaskResponse taskResponse=taskService.updateTask(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskResponse));
    }

    @Operation(summary = "Assign task", description = "Assign task to a project member")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Task state or assignee is invalid"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task or user not found")
    })
    @PatchMapping("/{taskId}/assign/{assigneeId}")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTask(@PathVariable("taskId") Long taskId,
                                        @PathVariable("assigneeId") Long assigneeId){
        TaskResponse taskResponse=taskService.assign(taskId,assigneeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskResponse));
    }

    @Operation(summary = "Start task", description = "Change task status to IN_PROGRESS")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task started successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Task has no assignee or invalid state"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/{id}/start")
    public ResponseEntity<ApiResponse<TaskResponse>> startTask(@PathVariable("id") Long id){
        TaskResponse taskResponse=taskService.startTask(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskResponse));
    }

    @Operation(summary = "Complete task", description = "Change task status to COMPLETED")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task completed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Task must be in progress before completion"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TaskResponse>> completeTask(@PathVariable("id") Long id){
        TaskResponse taskResponse=taskService.completeTask(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(taskResponse));
    }
}
