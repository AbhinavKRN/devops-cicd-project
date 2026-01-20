package com.devops.taskmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check controller for container runtime validation.
 * Essential for Kubernetes liveness and readiness probes.
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    private static final LocalDateTime START_TIME = LocalDateTime.now();

    /**
     * Health check endpoint for container validation.
     * @return health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "startedAt", START_TIME.toString(),
                "application", "TaskManager API",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(health);
    }

    /**
     * Readiness check for Kubernetes.
     * @return readiness status
     */
    @GetMapping("/ready")
    public ResponseEntity<Map<String, String>> ready() {
        return ResponseEntity.ok(Map.of(
                "status", "READY",
                "message", "Application is ready to accept traffic"
        ));
    }

    /**
     * Liveness check for Kubernetes.
     * @return liveness status
     */
    @GetMapping("/live")
    public ResponseEntity<Map<String, String>> live() {
        return ResponseEntity.ok(Map.of(
                "status", "ALIVE",
                "message", "Application is running"
        ));
    }
}

