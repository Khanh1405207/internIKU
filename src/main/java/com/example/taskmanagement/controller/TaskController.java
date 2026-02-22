package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.request.CreateTaskRequest;
import com.example.taskmanagement.dto.request.UpdateTaskRequest;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable("id") Long id){
        return ResponseEntity.ok(taskService.getTasksById(id));
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<?> getTasksByProject(@PathVariable("id") Long id){
        return ResponseEntity.ok(taskService.getTasksByProject(id));
    }

    @GetMapping("/assignee/{id}")
    public ResponseEntity<?> getTasksByAssignee(@PathVariable("id") Long id){
        return ResponseEntity.ok(taskService.getTasksByAssignee(id));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody CreateTaskRequest request){
        taskService.createTask(request);
        return ResponseEntity.status(201).body("Create task successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@Valid@RequestBody UpdateTaskRequest request,
                                        @PathVariable("id") Long id){
        request.setId(id);
        taskService.updateTask(request);
        return ResponseEntity.ok("Update task successfully");
    }

    @PatchMapping("/{taskId}/assign/{assigneeId}")
    public ResponseEntity<?> assignTask(@PathVariable("taskId") Long taskId,
                                        @PathVariable("assigneeId") Long assigneeId){
        taskService.assign(taskId,assigneeId);
        return ResponseEntity.ok("Assign successfully");
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<?> startTask(@PathVariable("id") Long id){
        taskService.startTask(id);
        return ResponseEntity.ok("Task start successfully");
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeTask(@PathVariable("id") Long id){
        taskService.completeTask(id);
        return ResponseEntity.ok("Task complete successfully");
    }
}
