# 📝 Task Management System

## Introduction

Welcome to the **Task Management System** project! This is a simple yet robust task management system developed using Java and Spring Boot. The system allows users to create, edit, delete, and view tasks, with functionalities such as assigning tasks, setting priorities, and leaving comments. The project is fully equipped with authentication and authorization mechanisms using JWT tokens, ensuring secure access to the API.

## Project Overview

### Features

- **Task Management:** Create, edit, delete, and view tasks.
- **User Management:** Authentication and authorization using JWT.
- **Task Assignment:** Assign tasks to users and manage their statuses.
- **Comments:** Leave comments on tasks.
- **Filtering & Pagination:** Easily filter tasks by author, assignee, or status with pagination support.
- **Error Handling:** Comprehensive error handling with meaningful messages.
- **API Documentation:** API is documented using OpenAPI and Swagger UI.

### Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Swagger UI**

## Getting Started

### Prerequisites

- **Docker**: Ensure Docker is installed on your machine.
- **Git**: Make sure you have Git installed to clone the repository.

### Clone the Repository

To get started, clone the repository to your local machine:

```bash
git clone https://github.com/your-username/task-management-system.git
cd task-management-system
```

### Run docker 

```bash
docker-compose up --build
```

### Open Swagger API
http://localhost:8080/swagger-ui.html

### If you wish test the application
https://zakyatbot.ru/swagger-ui.html
