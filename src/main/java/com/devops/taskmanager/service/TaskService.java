package com.devops.taskmanager.service;

import com.devops.taskmanager.model.Task;
import com.devops.taskmanager.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service class for managing tasks.
 * Uses in-memory storage for demonstration purposes.
 */
@Service
public class TaskService {

    private final Map<String, Task> taskStore = new ConcurrentHashMap<>();

    /**
     * Creates a new task.
     * @param task the task to create
     * @return the created task
     */
    public Task createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        taskStore.put(task.getId(), task);
        return task;
    }

    /**
     * Retrieves all tasks.
     * @return list of all tasks
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskStore.values());
    }

    /**
     * Retrieves a task by its ID.
     * @param id the task ID
     * @return optional containing the task if found
     */
    public Optional<Task> getTaskById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(taskStore.get(id));
    }

    /**
     * Updates an existing task.
     * @param id the task ID
     * @param updatedTask the updated task data
     * @return optional containing the updated task if found
     */
    public Optional<Task> updateTask(String id, Task updatedTask) {
        if (id == null || !taskStore.containsKey(id)) {
            return Optional.empty();
        }

        Task existingTask = taskStore.get(id);
        if (updatedTask.getTitle() != null) {
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getPriority() != null) {
            existingTask.setPriority(updatedTask.getPriority());
        }

        return Optional.of(existingTask);
    }

    /**
     * Deletes a task by its ID.
     * @param id the task ID
     * @return true if the task was deleted, false otherwise
     */
    public boolean deleteTask(String id) {
        if (id == null) {
            return false;
        }
        return taskStore.remove(id) != null;
    }

    /**
     * Retrieves tasks by status.
     * @param status the task status to filter by
     * @return list of tasks with the specified status
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskStore.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Gets the total count of tasks.
     * @return the number of tasks
     */
    public long getTaskCount() {
        return taskStore.size();
    }

    /**
     * Clears all tasks (useful for testing).
     */
    public void clearAllTasks() {
        taskStore.clear();
    }
}

