package com.example.taskmanagement.controller;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskStatus;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public static record CreateTaskRequest(
            @NotBlank String title,
            String description,
            TaskStatus status,
            @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {}

    public static record UpdateTaskRequest(
            String title,
            String description,
            TaskStatus status,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {}

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task created = taskService.createTask(
                request.title(),
                request.description(),
                request.status(),
                request.dueDate()
        );
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable String id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id,
                                           @RequestBody UpdateTaskRequest request) {
        Task updated = taskService.updateTask(
                id,
                Optional.ofNullable(request.title()),
                Optional.ofNullable(request.description()),
                Optional.ofNullable(request.status()),
                Optional.ofNullable(request.dueDate())
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Task>> listAll() {
        List<Task> tasks = taskService.listAllTasks();
        return ResponseEntity.ok(tasks);
    }
}
