package com.devops.taskmanager.controller;

import com.devops.taskmanager.model.Task;
import com.devops.taskmanager.model.TaskStatus;
import com.devops.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller tests for TaskController.
 * Validates REST API endpoints.
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/tasks should create a new task")
    void shouldCreateTask() throws Exception {
        Task task = new Task("New Task", "Task Description");
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Task Description"));
    }

    @Test
    @DisplayName("GET /api/v1/tasks should return all tasks")
    void shouldGetAllTasks() throws Exception {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} should return task by ID")
    void shouldGetTaskById() throws Exception {
        Task task = new Task("Test Task", "Description");
        when(taskService.getTaskById("test-id")).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/v1/tasks/test-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} should return 404 for non-existent task")
    void shouldReturn404ForNonExistentTask() throws Exception {
        when(taskService.getTaskById("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/tasks/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/tasks/{id} should update task")
    void shouldUpdateTask() throws Exception {
        Task task = new Task("Updated Task", "Updated Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskService.updateTask(eq("test-id"), any(Task.class))).thenReturn(Optional.of(task));

        mockMvc.perform(put("/api/v1/tasks/test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    @DisplayName("DELETE /api/v1/tasks/{id} should delete task")
    void shouldDeleteTask() throws Exception {
        when(taskService.deleteTask("test-id")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tasks/test-id"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/tasks/{id} should return 404 for non-existent task")
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        when(taskService.deleteTask("non-existent")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/tasks/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/tasks/stats should return statistics")
    void shouldGetStats() throws Exception {
        when(taskService.getTaskCount()).thenReturn(10L);
        when(taskService.getTasksByStatus(TaskStatus.PENDING)).thenReturn(Arrays.asList(new Task(), new Task()));
        when(taskService.getTasksByStatus(TaskStatus.COMPLETED)).thenReturn(Arrays.asList(new Task()));

        mockMvc.perform(get("/api/v1/tasks/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(10))
                .andExpect(jsonPath("$.pendingTasks").value(2))
                .andExpect(jsonPath("$.completedTasks").value(1));
    }
}

