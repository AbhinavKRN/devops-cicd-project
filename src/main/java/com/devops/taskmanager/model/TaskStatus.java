package com.devops.taskmanager.model;

/**
 * Enum representing the possible statuses of a task.
 */
public enum TaskStatus {
    /**
     * Task is pending and not yet started.
     */
    PENDING,

    /**
     * Task is currently in progress.
     */
    IN_PROGRESS,

    /**
     * Task has been completed.
     */
    COMPLETED,

    /**
     * Task has been cancelled.
     */
    CANCELLED
}

