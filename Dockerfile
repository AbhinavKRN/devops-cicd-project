# =============================================================================
# Multi-stage Dockerfile for TaskManager API
# Optimized for security, size, and production deployment
# =============================================================================

# Stage 1: Build Stage
# Uses Maven to compile and package the application
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

# Set working directory
WORKDIR /build

# Copy dependency files first (for layer caching)
COPY pom.xml .

# Download dependencies (cached layer if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
COPY checkstyle.xml .

# Build the application (skip tests as they run in CI pipeline)
RUN mvn clean package -DskipTests -B

# =============================================================================
# Stage 2: Runtime Stage
# Uses distroless/minimal image for security
# =============================================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Security: Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose application port
EXPOSE 8080

# Health check for container orchestration
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/v1/health || exit 1

# JVM optimizations for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# =============================================================================
# Labels for container metadata
# =============================================================================
LABEL maintainer="DevOps Team"
LABEL version="1.0.0"
LABEL description="TaskManager API - Production Grade REST API"
LABEL org.opencontainers.image.source="https://github.com/username/devops-assignment"

