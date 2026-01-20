package com.devops.taskmanager.service;

import com.devops.taskmanager.model.Task;
import com.devops.taskmanager.model.TaskPriority;
import com.devops.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for TaskService.
 * Validates business logic for task management operations.
 */
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskService.clearAllTasks();
    }

    @Test
    @DisplayName("Should create a new task successfully")
    void shouldCreateTask() {
        Task task = new Task("Test Task", "Test Description");

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertNotNull(createdTask.getId());
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertEquals(TaskStatus.PENDING, createdTask.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when creating null task")
    void shouldThrowExceptionForNullTask() {
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(null));
    }

    @Test
    @DisplayName("Should retrieve all tasks")
    void shouldGetAllTasks() {
        taskService.createTask(new Task("Task 1", "Description 1"));
        taskService.createTask(new Task("Task 2", "Description 2"));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("Should retrieve task by ID")
    void shouldGetTaskById() {
        Task task = new Task("Test Task", "Test Description");
        taskService.createTask(task);

        Optional<Task> foundTask = taskService.getTaskById(task.getId());

        assertTrue(foundTask.isPresent());
        assertEquals(task.getId(), foundTask.get().getId());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent task")
    void shouldReturnEmptyForNonExistentTask() {
        Optional<Task> foundTask = taskService.getTaskById("non-existent-id");

        assertFalse(foundTask.isPresent());
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTask() {
        Task task = new Task("Original Title", "Original Description");
        taskService.createTask(task);

        Task updatedData = new Task();
        updatedData.setTitle("Updated Title");
        updatedData.setStatus(TaskStatus.IN_PROGRESS);

        Optional<Task> updatedTask = taskService.updateTask(task.getId(), updatedData);

        assertTrue(updatedTask.isPresent());
        assertEquals("Updated Title", updatedTask.get().getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.get().getStatus());
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTask() {
        Task task = new Task("Task to Delete", "Description");
        taskService.createTask(task);

        boolean deleted = taskService.deleteTask(task.getId());

        assertTrue(deleted);
        assertEquals(0, taskService.getTaskCount());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent task")
    void shouldReturnFalseForNonExistentDelete() {
        boolean deleted = taskService.deleteTask("non-existent-id");

        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should filter tasks by status")
    void shouldFilterTasksByStatus() {
        Task pendingTask = new Task("Pending Task", "Description");
        Task completedTask = new Task("Completed Task", "Description");
        completedTask.setStatus(TaskStatus.COMPLETED);

        taskService.createTask(pendingTask);
        taskService.createTask(completedTask);

        List<Task> pendingTasks = taskService.getTasksByStatus(TaskStatus.PENDING);
        List<Task> completedTasks = taskService.getTasksByStatus(TaskStatus.COMPLETED);

        assertEquals(1, pendingTasks.size());
        assertEquals(1, completedTasks.size());
    }

    @Test
    @DisplayName("Should return correct task count")
    void shouldReturnCorrectTaskCount() {
        assertEquals(0, taskService.getTaskCount());

        taskService.createTask(new Task("Task 1", "Description 1"));
        taskService.createTask(new Task("Task 2", "Description 2"));

        assertEquals(2, taskService.getTaskCount());
    }

    @Test
    @DisplayName("Should handle task priority")
    void shouldHandleTaskPriority() {
        Task task = new Task("Priority Task", "Description");
        task.setPriority(TaskPriority.CRITICAL);
        taskService.createTask(task);

        Optional<Task> foundTask = taskService.getTaskById(task.getId());

        assertTrue(foundTask.isPresent());
        assertEquals(TaskPriority.CRITICAL, foundTask.get().getPriority());
    }
}

