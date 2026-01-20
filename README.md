# TaskManager API - DevOps CI/CD Project

A production-grade Task Management REST API demonstrating comprehensive CI/CD practices, DevSecOps principles, and Kubernetes deployment.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [CI/CD Pipeline](#cicd-pipeline)
- [Technology Stack](#technology-stack)
- [Local Development](#local-development)
- [Docker Usage](#docker-usage)
- [Kubernetes Deployment](#kubernetes-deployment)
- [GitHub Secrets Configuration](#github-secrets-configuration)
- [API Documentation](#api-documentation)

## 🎯 Project Overview

This project implements a complete CI/CD pipeline for a Java Spring Boot application, incorporating:

- **Continuous Integration (CI)**: Automated builds, testing, and security scanning
- **Code Quality**: Checkstyle linting and code coverage
- **DevSecOps**: SAST (CodeQL), SCA (OWASP Dependency Check), Container Scanning (Trivy)
- **Containerization**: Multi-stage Docker builds with security best practices
- **Continuous Deployment (CD)**: Kubernetes deployment with DAST

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           CI/CD PIPELINE                                │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐          │
│  │  Commit  │───▶│  Build   │───▶│   Test   │───▶│   Lint   │          │
│  └──────────┘    └──────────┘    └──────────┘    └──────────┘          │
│                                                         │               │
│                                                         ▼               │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                    SECURITY SCANNING                              │  │
│  │  ┌────────────┐    ┌────────────┐    ┌────────────┐              │  │
│  │  │   SAST     │    │    SCA     │    │  Container │              │  │
│  │  │  (CodeQL)  │    │  (OWASP)   │    │   (Trivy)  │              │  │
│  │  └────────────┘    └────────────┘    └────────────┘              │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                   │                                     │
│                                   ▼                                     │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐          │
│  │  Docker  │───▶│  Smoke   │───▶│   Push   │───▶│  Deploy  │          │
│  │  Build   │    │   Test   │    │ Registry │    │   K8s    │          │
│  └──────────┘    └──────────┘    └──────────┘    └──────────┘          │
│                                                         │               │
│                                                         ▼               │
│                                                  ┌──────────┐          │
│                                                  │   DAST   │          │
│                                                  │  (ZAP)   │          │
│                                                  └──────────┘          │
└─────────────────────────────────────────────────────────────────────────┘
```

## 🔄 CI/CD Pipeline

### CI Pipeline Stages

| Stage | Tool | Purpose | Risk Mitigated |
|-------|------|---------|----------------|
| **Checkout** | GitHub Actions | Retrieve source code | Consistent source |
| **Setup Runtime** | Temurin JDK 17 | Install Java runtime | Version consistency |
| **Linting** | Checkstyle | Enforce coding standards | Technical debt |
| **Unit Tests** | JUnit 5 + JaCoCo | Validate business logic | Regressions |
| **SAST** | CodeQL | Detect code vulnerabilities | OWASP Top 10 |
| **SCA** | OWASP Dependency Check | Scan dependencies | Supply chain risks |
| **Build** | Maven | Package application | Build failures |
| **Docker Build** | Docker | Create container image | Container issues |
| **Container Scan** | Trivy | Detect image vulnerabilities | Vulnerable images |
| **Runtime Test** | Docker + curl | Validate container behavior | Broken containers |
| **Push Registry** | Docker Hub | Publish trusted image | Deployment readiness |

### CD Pipeline Stages

| Stage | Purpose |
|-------|---------|
| **Pre-Deployment Validation** | Verify image exists and is valid |
| **Deploy to Kubernetes** | Apply manifests to cluster |
| **DAST Scan** | Dynamic security testing |
| **Smoke Tests** | Verify deployment works |

## 🛠️ Technology Stack

| Category | Technology |
|----------|------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2 |
| **Build Tool** | Maven |
| **Testing** | JUnit 5, MockMvc |
| **Code Quality** | Checkstyle, JaCoCo |
| **SAST** | GitHub CodeQL |
| **SCA** | OWASP Dependency Check |
| **Container** | Docker (Multi-stage) |
| **Container Scan** | Trivy |
| **DAST** | OWASP ZAP |
| **Orchestration** | Kubernetes |
| **CI/CD** | GitHub Actions |
| **Registry** | Docker Hub |

## 💻 Local Development

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker (optional)

### Build and Run

```bash
# Clone the repository
git clone https://github.com/yourusername/devops-assignment.git
cd devops-assignment

# Build the application
mvn clean package

# Run the application
java -jar target/taskmanager-api-1.0.0.jar

# Or using Maven
mvn spring-boot:run
```

### Run Tests

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# Run linting
mvn checkstyle:check

# Run dependency check
mvn org.owasp:dependency-check-maven:check
```

## 🐳 Docker Usage

### Build Docker Image

```bash
# Build the image
docker build -t taskmanager-api:latest .

# Run the container
docker run -d -p 8080:8080 --name taskmanager taskmanager-api:latest

# View logs
docker logs -f taskmanager

# Health check
curl http://localhost:8080/api/v1/health
```

### Docker Compose (Optional)

```yaml
version: '3.8'
services:
  taskmanager:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    healthcheck:
      test: ["CMD", "wget", "--spider", "-q", "http://localhost:8080/api/v1/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

## ☸️ Kubernetes Deployment

### Apply Manifests

```bash
# Create namespace
kubectl apply -f k8s/namespace.yaml

# Apply all resources
kubectl apply -f k8s/

# Verify deployment
kubectl get pods -n taskmanager
kubectl get services -n taskmanager

# View logs
kubectl logs -f deployment/taskmanager-api -n taskmanager
```

### Port Forward for Local Testing

```bash
kubectl port-forward service/taskmanager-api 8080:80 -n taskmanager
```

## 🔐 GitHub Secrets Configuration

Configure the following secrets in your GitHub repository:

| Secret Name | Description | Required |
|-------------|-------------|----------|
| `DOCKERHUB_USERNAME` | Docker Hub username | Yes |
| `DOCKERHUB_TOKEN` | Docker Hub access token | Yes |
| `KUBE_CONFIG` | Base64-encoded kubeconfig | For CD |

### Setting Up Secrets

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add each secret with its value

### Creating Docker Hub Token

1. Log in to [Docker Hub](https://hub.docker.com)
2. Go to **Account Settings** → **Security**
3. Click **New Access Token**
4. Provide a description and select permissions
5. Copy the generated token

### Creating Kubeconfig Secret

```bash
# Encode your kubeconfig
cat ~/.kube/config | base64 -w 0
```

## 📡 API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Endpoints

#### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/tasks` | Get all tasks |
| `GET` | `/tasks/{id}` | Get task by ID |
| `POST` | `/tasks` | Create new task |
| `PUT` | `/tasks/{id}` | Update task |
| `DELETE` | `/tasks/{id}` | Delete task |
| `GET` | `/tasks/stats` | Get task statistics |

#### Health Checks

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/health` | Application health |
| `GET` | `/ready` | Readiness check |
| `GET` | `/live` | Liveness check |

### Example Requests

```bash
# Create a task
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Complete DevOps Project", "description": "Implement CI/CD pipeline"}'

# Get all tasks
curl http://localhost:8080/api/v1/tasks

# Health check
curl http://localhost:8080/api/v1/health
```

### Response Examples

#### Task Object
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Complete DevOps Project",
  "description": "Implement CI/CD pipeline",
  "status": "PENDING",
  "priority": "MEDIUM",
  "createdAt": "2026-01-20T10:30:00",
  "updatedAt": "2026-01-20T10:30:00"
}
```

#### Health Response
```json
{
  "status": "UP",
  "timestamp": "2026-01-20T10:30:00",
  "startedAt": "2026-01-20T10:00:00",
  "application": "TaskManager API",
  "version": "1.0.0"
}
```

## 📁 Project Structure

```
project-root/
├── .github/
│   └── workflows/
│       ├── ci.yml              # CI Pipeline
│       └── cd.yml              # CD Pipeline
├── .zap/
│   └── rules.tsv               # OWASP ZAP configuration
├── k8s/
│   ├── namespace.yaml          # Kubernetes namespace
│   ├── configmap.yaml          # Configuration
│   ├── deployment.yaml         # Deployment manifest
│   └── service.yaml            # Service & Ingress
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/devops/taskmanager/
│   │   │       ├── TaskManagerApplication.java
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/devops/taskmanager/
├── Dockerfile                  # Multi-stage Docker build
├── pom.xml                     # Maven configuration
├── checkstyle.xml              # Code style rules
└── README.md                   # This file
```

## 👥 Author

DevOps CI/CD Project - Scaler Academy

## 📄 License

This project is for educational purposes as part of the DevOps assessment.

