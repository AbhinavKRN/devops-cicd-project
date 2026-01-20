# DevOps CI/CD Project Proposal

---

## Project Title

**TaskManager API: Production-Grade CI/CD Pipeline with DevSecOps Integration**

---

## Student Information

| Field | Details |
|-------|---------|
| **Name** | Abhinav Kumar Narayan |
| **Scaler Student ID** | 23bcs10014 |
| **Submission Date** | January 20, 2026 |
| **Project Type** | Individual |

---

## GitHub Repository URL

**Repository:** https://github.com/AbhinavKRN/devops-cicd-project
---

## 1. Application Description

### 1.1 Application Overview

**TaskManager API** is a production-grade RESTful web service built using Java Spring Boot 3.2. The application provides a complete task management system with CRUD operations, designed to demonstrate real-world CI/CD practices.

### 1.2 Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Programming Language | Java | 17 (LTS) |
| Framework | Spring Boot | 3.2.0 |
| Build Tool | Apache Maven | 3.9.x |
| Testing | JUnit 5, MockMvc | 5.10.x |
| Code Quality | Checkstyle, JaCoCo | Latest |
| Containerization | Docker | Multi-stage |
| Orchestration | Kubernetes | 1.28+ |
| CI/CD Platform | GitHub Actions | Latest |

### 1.3 Application Features

- **Task Management**: Create, Read, Update, Delete (CRUD) operations
- **Status Tracking**: PENDING, IN_PROGRESS, COMPLETED, CANCELLED
- **Priority Levels**: LOW, MEDIUM, HIGH, CRITICAL
- **Health Endpoints**: /health, /ready, /live for Kubernetes probes
- **Statistics API**: Real-time task metrics and counts
- **Input Validation**: Jakarta Bean Validation for data integrity

### 1.4 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/tasks | Create new task |
| GET | /api/v1/tasks | List all tasks |
| GET | /api/v1/tasks/{id} | Get task by ID |
| PUT | /api/v1/tasks/{id} | Update task |
| DELETE | /api/v1/tasks/{id} | Delete task |
| GET | /api/v1/tasks/stats | Get statistics |
| GET | /api/v1/health | Health check |
| GET | /api/v1/ready | Readiness probe |
| GET | /api/v1/live | Liveness probe |

---

## 2. CI/CD Problem Statement

### 2.1 Industry Context

Modern software development faces critical challenges in delivering reliable, secure, and high-quality applications at speed. Organizations struggle with:

1. **Manual Deployment Risks**: Human errors during deployment cause 70% of outages
2. **Security Vulnerabilities**: 84% of codebases contain open-source vulnerabilities (Synopsys 2024)
3. **Quality Degradation**: Without automated testing, bugs escape to production
4. **Slow Feedback Loops**: Developers wait hours or days for deployment feedback
5. **Inconsistent Environments**: "Works on my machine" syndrome

### 2.2 Problem Statement

> **How can we design and implement a production-grade CI/CD pipeline that automatically builds, tests, secures, and deploys a Java application while ensuring code quality, detecting security vulnerabilities at multiple layers, and enabling zero-downtime deployments to Kubernetes?**

### 2.3 Specific Challenges Addressed

| Challenge | Impact | Solution |
|-----------|--------|----------|
| Manual testing | Regressions reach production | Automated unit testing with coverage gates |
| Code quality drift | Technical debt accumulates | Automated linting with Checkstyle |
| Vulnerable code | Security breaches | SAST with GitHub CodeQL |
| Vulnerable dependencies | Supply chain attacks | SCA with OWASP Dependency Check |
| Insecure containers | Runtime vulnerabilities | Container scanning with Trivy |
| Deployment failures | Service outages | Kubernetes rolling updates |
| Runtime vulnerabilities | Production exploits | DAST with OWASP ZAP |

### 2.4 DevSecOps Philosophy

This project embraces the **Shift-Left Security** principle:

```
Traditional: Code → Build → Test → Deploy → Security → Production
Shift-Left:  Code → Security → Build → Security → Test → Security → Deploy → Security
```

By integrating security at every stage, vulnerabilities are detected when they're cheapest to fix—during development, not after deployment.

---

## 3. Chosen CI/CD Stages and Justification

### 3.1 CI Pipeline Stages

#### Stage 1: Source Code Checkout
| Attribute | Detail |
|-----------|--------|
| **Tool** | GitHub Actions checkout@v4 |
| **Purpose** | Retrieve source code from repository |
| **Why Required** | Foundation for all subsequent stages; ensures consistent source |
| **Risk Mitigated** | Building from incorrect or corrupted code |

#### Stage 2: Environment Setup (Java Runtime)
| Attribute | Detail |
|-----------|--------|
| **Tool** | Temurin JDK 17 |
| **Purpose** | Install consistent Java runtime |
| **Why Required** | Eliminates "works on my machine" issues |
| **Risk Mitigated** | Version incompatibilities, build failures |

#### Stage 3: Dependency Caching
| Attribute | Detail |
|-----------|--------|
| **Tool** | GitHub Actions cache |
| **Purpose** | Cache Maven dependencies |
| **Why Required** | Reduces build time from ~5 min to ~1 min |
| **Benefit** | Faster feedback loops, cost savings |

#### Stage 4: Code Linting (Checkstyle)
| Attribute | Detail |
|-----------|--------|
| **Tool** | Maven Checkstyle Plugin |
| **Purpose** | Enforce Java coding standards |
| **Why Required** | Prevents technical debt accumulation |
| **Risk Mitigated** | Inconsistent code style, maintainability issues |
| **Shift-Left** | Catches issues before code review |

#### Stage 5: Unit Testing with Coverage
| Attribute | Detail |
|-----------|--------|
| **Tool** | JUnit 5, JaCoCo |
| **Purpose** | Validate business logic, measure coverage |
| **Why Required** | Prevents regressions from reaching production |
| **Risk Mitigated** | Broken functionality, untested code paths |
| **Quality Gate** | Minimum 50% line coverage required |

#### Stage 6: SAST (Static Application Security Testing)
| Attribute | Detail |
|-----------|--------|
| **Tool** | GitHub CodeQL |
| **Purpose** | Detect code-level security vulnerabilities |
| **Why Required** | Identifies OWASP Top 10 vulnerabilities |
| **Risk Mitigated** | SQL Injection, XSS, Command Injection, Path Traversal |
| **Output** | SARIF results in GitHub Security tab |

#### Stage 7: SCA (Software Composition Analysis)
| Attribute | Detail |
|-----------|--------|
| **Tool** | OWASP Dependency Check |
| **Purpose** | Scan third-party dependencies for CVEs |
| **Why Required** | 84% of codebases have vulnerable dependencies |
| **Risk Mitigated** | Supply chain attacks (Log4Shell, etc.) |
| **Quality Gate** | Fails on CVSS ≥ 7 (High/Critical) |

#### Stage 8: Application Build
| Attribute | Detail |
|-----------|--------|
| **Tool** | Apache Maven |
| **Purpose** | Compile and package application |
| **Why Required** | Creates deployable JAR artifact |
| **Output** | taskmanager-api-1.0.0.jar |

#### Stage 9: Docker Image Build
| Attribute | Detail |
|-----------|--------|
| **Tool** | Docker with multi-stage build |
| **Purpose** | Create optimized container image |
| **Why Required** | Enables consistent deployment across environments |
| **Security Features** | Non-root user, minimal Alpine base, no build tools |

#### Stage 10: Container Vulnerability Scan
| Attribute | Detail |
|-----------|--------|
| **Tool** | Aqua Trivy |
| **Purpose** | Scan image for OS and library vulnerabilities |
| **Why Required** | Base images often contain CVEs |
| **Risk Mitigated** | Vulnerable containers shipping to production |
| **Output** | SARIF results in GitHub Security tab |

#### Stage 11: Container Runtime Test
| Attribute | Detail |
|-----------|--------|
| **Tool** | Docker, curl |
| **Purpose** | Validate container starts and responds |
| **Why Required** | Catches configuration issues before deployment |
| **Risk Mitigated** | Non-functional containers |

#### Stage 12: Registry Push
| Attribute | Detail |
|-----------|--------|
| **Tool** | Docker Hub |
| **Purpose** | Publish verified image to registry |
| **Why Required** | Makes image available for deployment |
| **Security** | Only after all scans pass; secrets in GitHub Secrets |

### 3.2 CD Pipeline Stages

#### Stage 1: Pre-Deployment Validation
| Attribute | Detail |
|-----------|--------|
| **Purpose** | Verify image exists and is valid |
| **Why Required** | Prevents deploying non-existent images |

#### Stage 2: Kubernetes Deployment
| Attribute | Detail |
|-----------|--------|
| **Tool** | kubectl |
| **Purpose** | Deploy application to Kubernetes |
| **Strategy** | Rolling update for zero-downtime |
| **Features** | Liveness/Readiness probes, resource limits, HPA |

#### Stage 3: DAST (Dynamic Application Security Testing)
| Attribute | Detail |
|-----------|--------|
| **Tool** | OWASP ZAP |
| **Purpose** | Test running application for vulnerabilities |
| **Why Required** | Detects runtime security issues missed by SAST |
| **Difference from SAST** | Tests actual behavior, not just code |

#### Stage 4: Smoke Tests
| Attribute | Detail |
|-----------|--------|
| **Purpose** | Verify deployed application works correctly |
| **Tests** | Health endpoints, API functionality |

---

## 4. Expected Outcomes

### 4.1 Primary Outcomes

| Outcome | Metric | Target |
|---------|--------|--------|
| **Automated Builds** | Manual intervention | Zero |
| **Test Coverage** | Line coverage | ≥ 50% |
| **Security Scans** | Vulnerability detection | SAST + SCA + Container + DAST |
| **Deployment Time** | End-to-end pipeline | < 15 minutes |
| **Deployment Success Rate** | Successful deployments | > 95% |

### 4.2 Security Outcomes

| Security Control | Implementation |
|------------------|----------------|
| SAST | CodeQL detecting OWASP Top 10 |
| SCA | OWASP Dependency Check for CVEs |
| Container Security | Trivy scanning images |
| DAST | OWASP ZAP baseline scan |
| Secret Management | GitHub Secrets (no hardcoding) |
| Container Hardening | Non-root user, minimal base image |

### 4.3 DevOps Best Practices Demonstrated

1. **Infrastructure as Code**: Kubernetes manifests in version control
2. **GitOps**: Pipeline triggered by Git events
3. **Immutable Infrastructure**: Container images tagged by commit SHA
4. **12-Factor App**: Externalized configuration via ConfigMaps
5. **Observability**: Health endpoints for monitoring
6. **Zero-Downtime Deployment**: Kubernetes rolling updates

### 4.4 Deliverables

| Deliverable | Description |
|-------------|-------------|
| Source Code | Java Spring Boot REST API |
| CI Pipeline | GitHub Actions workflow (ci.yml) |
| CD Pipeline | GitHub Actions workflow (cd.yml) |
| Dockerfile | Multi-stage production build |
| K8s Manifests | Deployment, Service, ConfigMap, HPA |
| Documentation | README with setup instructions |
| Security Reports | CodeQL, Dependency Check, Trivy results |

---

## 5. Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CI/CD PIPELINE                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   DEVELOPER         VERSION CONTROL         CI PIPELINE                     │
│   ┌───────┐         ┌────────────┐         ┌────────────────────────────┐  │
│   │ Code  │────────▶│   GitHub   │────────▶│ Build → Test → Lint       │  │
│   │ Push  │         │ Repository │         │         ↓                  │  │
│   └───────┘         └────────────┘         │ SAST (CodeQL)              │  │
│                                            │         ↓                  │  │
│                                            │ SCA (Dependency Check)     │  │
│                                            │         ↓                  │  │
│                                            │ Docker Build               │  │
│                                            │         ↓                  │  │
│                                            │ Container Scan (Trivy)     │  │
│                                            │         ↓                  │  │
│                                            │ Runtime Test               │  │
│                                            │         ↓                  │  │
│                                            │ Push to Docker Hub         │  │
│                                            └────────────────────────────┘  │
│                                                        │                    │
│   CD PIPELINE                                          ▼                    │
│   ┌────────────────────────────────────────────────────────────────────┐   │
│   │ Validate Image → Deploy to K8s → DAST Scan → Smoke Tests          │   │
│   └────────────────────────────────────────────────────────────────────┘   │
│                                            │                                │
│                                            ▼                                │
│   KUBERNETES CLUSTER                                                        │
│   ┌────────────────────────────────────────────────────────────────────┐   │
│   │  ┌─────────┐    ┌─────────┐    ┌─────────┐                         │   │
│   │  │  Pod 1  │    │  Pod 2  │    │  Pod 3  │  ← Rolling Updates     │   │
│   │  └─────────┘    └─────────┘    └─────────┘                         │   │
│   │        ↓              ↓              ↓                              │   │
│   │  ┌─────────────────────────────────────────────────────────────┐   │   │
│   │  │                    Service (Load Balancer)                   │   │   │
│   │  └─────────────────────────────────────────────────────────────┘   │   │
│   └────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## 6. References

1. GitHub Actions Documentation - https://docs.github.com/en/actions
2. OWASP Top 10 - https://owasp.org/www-project-top-ten/
3. Docker Best Practices - https://docs.docker.com/develop/dev-best-practices/
4. Kubernetes Documentation - https://kubernetes.io/docs/
5. Spring Boot Reference - https://docs.spring.io/spring-boot/docs/current/reference/html/
6. DevSecOps Principles - https://www.devsecops.org/

---

## 7. Conclusion

This project demonstrates a comprehensive understanding of modern CI/CD practices, incorporating:

- **Automation**: Every stage from build to deployment is automated
- **Security**: Multiple security scans at different layers (shift-left)
- **Quality**: Linting, testing, and coverage gates ensure code quality
- **Reliability**: Kubernetes enables self-healing and zero-downtime deployments
- **Observability**: Health endpoints enable monitoring and alerting

The pipeline is designed not just to "make the build green" but to understand **why each stage exists** and **what risks it mitigates**—reflecting true DevOps and DevSecOps maturity.

---

**Submitted By:** Abhinav Kumar Narayan 
**Student ID:** 23bcs10014
**Date:** January 20, 2026

---

