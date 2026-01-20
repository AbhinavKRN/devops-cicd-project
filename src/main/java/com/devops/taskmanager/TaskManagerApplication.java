package com.devops.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for TaskManager API.
 * This is a production-grade REST API for task management,
 * designed to demonstrate DevOps CI/CD best practices.
 */
@SpringBootApplication
public class TaskManagerApplication {

    /**
     * Main entry point for the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }
}

