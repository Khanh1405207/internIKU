package com.example.taskmanagement.serviceTest;

import com.example.taskmanagement.dto.request.CreateTaskRequest;
import com.example.taskmanagement.dto.request.UpdateTaskRequest;
import com.example.taskmanagement.dto.response.TaskResponse;
import com.example.taskmanagement.entity.Enum.TaskStatus;
import com.example.taskmanagement.entity.ProjectEntity;
import com.example.taskmanagement.entity.TaskEntity;
import com.example.taskmanagement.entity.UserEntity;
import com.example.taskmanagement.exception.BadRequestException;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

        private static final long TASK_ID = 1L;
        private static final long USER_ID = 2L;
        private static final long PROJECT_ID = 1L;
        private static final long ONE_HOUR_SECONDS = 3600L;
        private static final String UPDATED_TITLE = "Updated title";

    @Mock
    TaskRepository taskRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    TaskService taskService;

    // ======================== createTask ========================

    @Test
    public void createTask_success(){
        stubProjectFound(projectWithId(PROJECT_ID));
        when(taskRepository.save(any(TaskEntity.class)))
                .thenReturn(new TaskEntity());

        CreateTaskRequest request = createTaskRequest(futureDeadline());
        TaskResponse task=taskService.createTask(request);

        Assertions.assertNotNull(task);
        verify(projectRepository).findById(anyLong());
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    public void createTask_projectNotFound_throwException(){
        stubProjectNotFound();

        CreateTaskRequest request = createTaskRequest(futureDeadline());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.createTask(request));

        verify(projectRepository).findById(anyLong());
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    public void createTask_deadlineNotFuture_throwException(){
        stubProjectFound(projectWithId(PROJECT_ID));

        CreateTaskRequest request = createTaskRequest(pastDeadline());

        Assertions.assertThrows(BadRequestException.class,
                () -> taskService.createTask(request));

        verify(projectRepository).findById(anyLong());
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    // ======================== updateTask ========================

    @Test
    public void updateTask_success(){
                TaskEntity task = taskWithProject();
                stubTaskFound(task);

                UpdateTaskRequest request = updateTaskRequest(futureDeadline());

        TaskResponse response=taskService.updateTask(request);

        Assertions.assertNotNull(response);
                Assertions.assertEquals(UPDATED_TITLE, response.getTitle());
        verify(taskRepository).findById(anyLong());
    }

    @Test
    public void updateTask_taskNotFound_throwException(){
        stubTaskNotFound();

        UpdateTaskRequest request = updateTaskRequest(futureDeadline());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(request));

        verify(taskRepository).findById(anyLong());
    }

    @Test
    public void updateTask_deadlineNotFuture_throwException(){
        TaskEntity task = taskWithProject();
        stubTaskFound(task);

        UpdateTaskRequest request = updateTaskRequest(pastDeadline());

        Assertions.assertThrows(BadRequestException.class,
                () -> taskService.updateTask(request));

        verify(taskRepository).findById(anyLong());
    }

    // ======================== assign ========================

    @Test
    public void assign_success(){
                TaskEntity task = taskWithProject();
                stubTaskFound(task);
                stubUserFound();
                stubProjectMembership(true);

                TaskResponse response=taskService.assign(TASK_ID, USER_ID);

        Assertions.assertNotNull(response);
                verifyTaskAndUserLookupCalled();
        verify(projectRepository).existsByIdAndUsers_Id(anyLong(), anyLong());
    }

    @Test
    public void assign_taskNotFound_throwException(){
        stubTaskNotFound();

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.assign(TASK_ID, USER_ID));

        verify(taskRepository).findById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(projectRepository, never()).existsByIdAndUsers_Id(anyLong(), anyLong());
    }

    @Test
    public void assign_userNotFound_throwException(){
        stubTaskFound(taskWithProject());
        stubUserNotFound();

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.assign(TASK_ID, USER_ID));

        verifyTaskAndUserLookupCalled();
        verify(projectRepository, never()).existsByIdAndUsers_Id(anyLong(), anyLong());
    }

    @Test
    public void assign_taskNotTodo_throwException(){
        TaskEntity task = taskWithStatus(TaskStatus.IN_PROGRESS, futureDeadline());
        stubTaskFound(task);
        stubUserFound();

        Assertions.assertThrows(BadRequestException.class,
                () -> taskService.assign(TASK_ID, USER_ID));

        verifyTaskAndUserLookupCalled();
        verify(projectRepository, never()).existsByIdAndUsers_Id(anyLong(), anyLong());
    }

    @Test
    public void assign_taskIsOverdue_throwException(){
        TaskEntity task = taskWithStatus(TaskStatus.TODO, pastDeadline());
        stubTaskFound(task);
        stubUserFound();

        Assertions.assertThrows(BadRequestException.class,
                () -> taskService.assign(TASK_ID, USER_ID));

        verifyTaskAndUserLookupCalled();
        verify(projectRepository, never()).existsByIdAndUsers_Id(anyLong(), anyLong());
    }

    @Test
    public void assign_userNotInProject_throwException(){
                TaskEntity task = taskWithProject();
                stubTaskFound(task);
                stubUserFound();
                stubProjectMembership(false);

        Assertions.assertThrows(BadRequestException.class,
                                () -> taskService.assign(TASK_ID, USER_ID));

                verifyTaskAndUserLookupCalled();
        verify(projectRepository).existsByIdAndUsers_Id(anyLong(), anyLong());
    }

        private Instant futureDeadline() {
                return Instant.now().plusSeconds(ONE_HOUR_SECONDS);
        }

        private Instant pastDeadline() {
                return Instant.now().minusSeconds(ONE_HOUR_SECONDS);
        }

        private CreateTaskRequest createTaskRequest(Instant deadline) {
                CreateTaskRequest request = new CreateTaskRequest();
                request.setDeadline(deadline);
                request.setProjectId(PROJECT_ID);
                return request;
        }

        private UpdateTaskRequest updateTaskRequest(Instant deadline) {
                UpdateTaskRequest request = new UpdateTaskRequest();
                request.setId(TASK_ID);
                request.setTitle(UPDATED_TITLE);
                request.setDeadline(deadline);
                return request;
        }

        private TaskEntity taskWithStatus(TaskStatus status, Instant deadline) {
                TaskEntity task = new TaskEntity();
                task.setTaskStatus(status);
                task.setDeadline(deadline);
                return task;
        }

        private TaskEntity taskWithProject() {
                TaskEntity task = taskWithStatus(TaskStatus.TODO, futureDeadline());
                task.setProjectEntity(projectWithId(PROJECT_ID));
                return task;
        }

        private ProjectEntity projectWithId(long id) {
                ProjectEntity project = new ProjectEntity();
                project.setId(id);
                return project;
        }

        private void stubProjectFound(ProjectEntity project) {
                when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        }

        private void stubProjectNotFound() {
                when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());
        }

        private void stubTaskFound(TaskEntity task) {
                when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        }

        private void stubTaskNotFound() {
                when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
        }

        private void stubUserFound() {
                when(userRepository.findById(anyLong())).thenReturn(Optional.of(new UserEntity()));
        }

        private void stubUserNotFound() {
                when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        }

        private void stubProjectMembership(boolean isMember) {
                when(projectRepository.existsByIdAndUsers_Id(anyLong(), anyLong())).thenReturn(isMember);
        }

        private void verifyTaskAndUserLookupCalled() {
                verify(taskRepository).findById(anyLong());
                verify(userRepository).findById(anyLong());
        }
}
