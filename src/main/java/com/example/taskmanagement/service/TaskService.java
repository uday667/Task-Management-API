package com.example.taskmanagement.service;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskStatus;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title, String description, TaskStatus status, LocalDate dueDate) {
        Task task = Task.builder()
                .id(java.util.UUID.randomUUID().toString())
                .title(title)
                .description(description)
                .status(status != null ? status : TaskStatus.PENDING)
                .dueDate(dueDate)
                .build();
        return taskRepository.save(task);
    }

    public Task getTask(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task updateTask(String id, Optional<String> title, Optional<String> description,
                           Optional<TaskStatus> status, Optional<LocalDate> dueDate) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        title.ifPresent(task::setTitle);
        description.ifPresent(task::setDescription);
        status.ifPresent(task::setStatus);
        dueDate.ifPresent(task::setDueDate);
        return taskRepository.save(task);
    }

    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    public List<Task> listAllTasks() {
        return taskRepository.findAllByOrderByDueDateAsc();
    }
}
