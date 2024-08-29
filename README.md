# Customer Management System

## Overview
This is a full-stack web application for managing customers, built using Spring Boot for the backend and Angular for the frontend. The project includes basic CRUD operations (Create, Read, Update, Delete) on customer data, which is stored in a PostgreSQL database. The application is containerized using Docker, making it easy to deploy and scale.

### Features
- CRUD Operations: Manage customer data efficiently.
- Backend: Developed using Spring Boot 3.3.3 and Java 21.
- Frontend: Developed using Angular 16.
- Database: PostgreSQL 14 is used as the database.
- Containerization: The backend, frontend, and database are containerized using Docker.
- Testing: Utilizes Testcontainers for integration testing with PostgreSQL.
- CI/CD: Integrated with SonarCloud for continuous integration, ensuring code quality and security.

### Prerequisites
Before running this project, ensure you have the following installed:

- **Java 21**
- **Node.js & npm (for Angular 16)**
- **Docker**
- **PostgreSQL 14**

### Build and Run with Docker
The application is fully containerized. You can easily run it using Docker Compose.

```
docker-compose up --build
```

### Access the Application
- Frontend: Navigate to http://localhost:4200 to access the Angular frontend.
- Backend: The Spring Boot backend will be available at http://localhost:8080.
- Database: PostgreSQL will be accessible at localhost:5432.

### CI and Code Quality
This project is integrated with GitHub Actions for CI and SonarCloud for code quality analysis. The build file contains configurations to automatically run tests and static analysis on each commit.

### Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.
