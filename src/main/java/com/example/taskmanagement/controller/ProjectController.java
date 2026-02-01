package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.request.CreateProjectRequest;
import com.example.taskmanagement.dto.request.UpdateProjectRequest;
import com.example.taskmanagement.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(){
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") Long id){
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody CreateProjectRequest request){
        projectService.createProject(request);
        return ResponseEntity.status(201).body("Create Project successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@Valid @RequestBody UpdateProjectRequest request,
                                           @PathVariable("id") Long id){
        request.setId(id);
        projectService.updateProject(request);
        return ResponseEntity.ok("Update Project successfully");
    }
}
