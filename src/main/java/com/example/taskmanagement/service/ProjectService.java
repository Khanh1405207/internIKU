package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.CreateProjectRequest;
import com.example.taskmanagement.dto.request.UpdateProjectRequest;
import com.example.taskmanagement.dto.response.ProjectResponse;
import com.example.taskmanagement.entity.Enum.UserStatus;
import com.example.taskmanagement.entity.ProjectEntity;
import com.example.taskmanagement.entity.UserEntity;
import com.example.taskmanagement.exception.BadRequestException;
import com.example.taskmanagement.exception.ConflictException;
import com.example.taskmanagement.exception.ResourceNotFoundException;
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
                () -> new ResourceNotFoundException("Project not found")
        );
        return new ProjectResponse(project);
    }

    public ProjectResponse createProject(CreateProjectRequest createProjectRequest){
        UserEntity userEntity=userRepository.findById(createProjectRequest.getCreatedBy()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        ProjectEntity project=new ProjectEntity(
                createProjectRequest.getName(),
                createProjectRequest.getDescription(),
                userEntity);
        projectRepository.save(project);
        return new ProjectResponse(project);
    }

    public ProjectResponse updateProject(UpdateProjectRequest updateProjectRequest){
        ProjectEntity project=projectRepository.findById(updateProjectRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Project not found")
        );
        project.setName(updateProjectRequest.getName());
        project.setDescription(updateProjectRequest.getDescription());
        return new ProjectResponse(project);
    }

    public ProjectResponse addMember(Long projectId,Long userId){
        ProjectEntity project= projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project not found")
        );
        UserEntity user= userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        if (UserStatus.INACTIVE.equals(user.getStatus())) {
            throw new BadRequestException("Cannot add inactive user to project");
        }
        if (project.getUsers().contains(user)){
            throw new ConflictException("User already in project");
        }
        project.addMember(user);
        return new ProjectResponse(project);
    }
}
