package com.example.taskmanagement.consoleLog;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ConsoleTestRunner implements CommandLineRunner {

    private final TaskService taskService;

    public ConsoleTestRunner(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("===== START CONSOLE TEST =====");

        // Tạo User
        User user = new User("Khanh", "khanh@gmail.com", "123456");
        user.setId(1L);
        taskService.addUser(user);
        System.out.println("User: "+user.getName());

        // Tạo Project
        Project project = new Project("Demo Project", "Console Test", user);
        project.setId(1L);
        taskService.addProject(project);
        System.out.println("Project: "+project.getName());

        // Tạo Task
        Task task = taskService.createTask(
                "Code feature",
                "Write business logic",
                Instant.now().plusSeconds(3600),
                1L
        );
        System.out.println("Task: "+task.getTitle());
        System.out.println("Task: "+task.getTaskStatus());

        // Assign
        taskService.assignTask(1L, task.getId());
        System.out.println("Task assignee: "+ task.getAssignee().getName());

        // Start
        taskService.startTask(task.getId());
        System.out.println("Task status: "+ task.getTaskStatus());

        // Complete
        taskService.completeTask(task.getId());
        System.out.println("Final Task Status: " + task.getTaskStatus());
        try {
            taskService.startTask(task.getId());
        }catch (IllegalStateException e){
            System.out.println(e.getMessage());
        }
        System.out.println("===== END TEST =====");
    }
}
