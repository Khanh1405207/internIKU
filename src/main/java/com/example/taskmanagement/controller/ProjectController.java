package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.ApiResponse;
import com.example.taskmanagement.dto.request.CreateProjectRequest;
import com.example.taskmanagement.dto.request.UpdateProjectRequest;
import com.example.taskmanagement.dto.response.ProjectResponse;
import com.example.taskmanagement.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(projectService.getAllProjects()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(projectService.getProjectById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid @RequestBody CreateProjectRequest request){
        ProjectResponse projectResponse=projectService.createProject(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.create(projectResponse));
    }

    @PostMapping("/{projectId}/members/{userId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> addMember(@PathVariable("projectId") Long projectId,
                                       @PathVariable("userId") Long userId){
        ProjectResponse projectResponse=projectService.addMember(projectId,userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(projectResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(@Valid @RequestBody UpdateProjectRequest request,
                                           @PathVariable("id") Long id){
        request.setId(id);
        ProjectResponse projectResponse=projectService.updateProject(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(projectResponse));
    }
}
