package com.example.taskmanagement.service;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskStatus;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_success() {
        Task task = Task.builder()
                .id("1")
                .title("Test")
                .description("Desc")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(1))
                .build();
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task created = taskService.createTask("Test", "Desc", null, LocalDate.now().plusDays(1));
        assertEquals("Test", created.getTitle());
        assertEquals(TaskStatus.PENDING, created.getStatus());
    }

    @Test
    void getTask_notFound_throws() {
        when(taskRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask("99"));
    }

    @Test
    void updateTask_partial() {
        Task existing = Task.builder()
                .id("1")
                .title("Old")
                .description("Old desc")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now())
                .build();
        when(taskRepository.findById("1")).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));
        Task updated = taskService.updateTask("1", Optional.of("New"), Optional.empty(),
                Optional.of(TaskStatus.DONE), Optional.empty());
        assertEquals("New", updated.getTitle());
        assertEquals("Old desc", updated.getDescription());
        assertEquals(TaskStatus.DONE, updated.getStatus());
    }

    @Test
    void deleteTask_exists() {
        when(taskRepository.existsById("1")).thenReturn(true);
        doNothing().when(taskRepository).deleteById("1");
        taskService.deleteTask("1");
        verify(taskRepository).deleteById("1");
    }

    @Test
    void deleteTask_notExists_throws() {
        when(taskRepository.existsById("2")).thenReturn(false);
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask("2"));
    }

    @Test
    void listAllTasks_sorted() {
        Task t1 = Task.builder().id("1").dueDate(LocalDate.now()).build();
        Task t2 = Task.builder().id("2").dueDate(LocalDate.now().plusDays(1)).build();
        when(taskRepository.findAllByOrderByDueDateAsc()).thenReturn(java.util.List.of(t1, t2));
        var list = taskService.listAllTasks();
        assertEquals(2, list.size());
        assertEquals("1", list.get(0).getId());
    }
}
