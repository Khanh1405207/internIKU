package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.CreateProjectRequest;
import com.example.taskmanagement.dto.request.UpdateProjectRequest;
import com.example.taskmanagement.dto.response.ProjectResponse;
import com.example.taskmanagement.entity.ProjectEntity;
import com.example.taskmanagement.entity.UserEntity;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectResponse> getAllProjects(){
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }

    public ProjectResponse getProjectById(Long id){
        ProjectEntity project=projectRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Project not found")
        );
        return new ProjectResponse(project);
    }

    public void createProject(CreateProjectRequest createProjectRequest){
        UserEntity userEntity=userRepository.findById(createProjectRequest.getCreatedBy()).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        ProjectEntity project=new ProjectEntity(
                createProjectRequest.getName(),
                createProjectRequest.getDescription(),
                userEntity);
        projectRepository.save(project);
    }

    public void updateProject(UpdateProjectRequest updateProjectRequest){
        ProjectEntity project=projectRepository.findById(updateProjectRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("Project not found")
        );
        project.setName(updateProjectRequest.getName());
        project.setDescription(updateProjectRequest.getDescription());
    }
}
