# 🧑‍💼 Employee Management System

## 📘 Overview
The **Employee Management System** is a **Spring Boot–based application** designed to manage employee data, departments, and hierarchical relationships between **administrators, managers, and employees**.

It includes:  
- **Role-based authentication** using JWT tokens  
- **CRUD operations** for employees, managers, and departments  
- **Kafka-based asynchronous messaging** for scalability  
- **PostgreSQL database integration**  
- **Swagger UI** for API documentation  
- **GitHub Actions workflow** for automated CI/CD builds  

This project follows a **clean modular architecture**, with clearly separated **services, repositories, and entities**.

---

## 🛠️ Technologies Used

| Category | Technology |
|----------|-----------|
| Backend Framework | Spring Boot |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | Hibernate / JPA |
| Messaging | Apache Kafka |
| Testing | JUnit, Mockito , Spring Boot Test |
| API Docs | Swagger / Springdoc OpenAPI |
| Build Tool | Maven |
| CI/CD | GitHub Actions |
| Version Control | Git & GitHub |

---

## ⚙️ Setup Instructions

### 1 Clone the Repository


### 2 Set Up Your DataBase and Docker

 Add the variables in the apllication.properties, application.yml
  and docker-compose.yml

Update  your local PostgreSQL credentials, for example:

POSTGRES_USER=postgres
POSTGRES_PASSWORD=yourpassword
POSTGRES_DB=employeedb

🗄️ Database Configuration


Example application.yml:
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/employeedb
    username: POSTGRES_USER
    password: POSTGRES_PASSWORD
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

        
### 3. Run Docker (Zookeeper and Kafka)
    To ensure Kafka can send and receive messages properly
      Note: Employee Services won't work without it

    To run Docker
    Use the this command promt -> docker-compose up -d

    Verify that the containers(Zookeeper and Kafka) are running -> docker ps 

### 4. Run the Application

    mvn spring-boot:run





### 🔐 Security & Authentication

The application uses JWT-based authentication via SecurityConfig.java.
Endpoints are protected based on user roles:

Role	Access
ADMIN	Full access to all endpoints
MANAGER	Can view employees in his department
EMPLOYEE	Can view his/her details only

JWT tokens are required for secured endpoints.
After login, include the token in your request header:

Authorization: Bearer <token>

🧩 Swagger API Documentation

After running the application, open:

http://localhost:8080/swagger-ui/index.html


🔐 To access protected endpoints (like viewing profiles), you’ll need to authorize Swagger with a valid JWT token.

🧠 Important Implementation Tips

💡 1. Assign Manager Before Employees

Always add a manager to a department before adding employees.
When you create an employee, the system checks if their department already has a manager.
If a manager exists, they are automatically linked to the employee.
Otherwise, the employee’s manager field will remain null.

💡 2. Auto-Manager Assignment

When a new manager is added to a department, all existing employees in that department without a manager are automatically updated to reference the new manager.

💡 3. Testing Controller Logic

Unit tests are written for all controllers and services using JUnit and Spring Boot Test.
To run tests locally:

mvn test


Tests automatically spin up an in-memory PostgreSQL container using GitHub Actions CI workflow.

💡 4. CI/CD Integration

Every push or pull request to main triggers:

Database via GitHub Actions

Maven build and tests

Workflow logs visible under the Actions tab on GitHub

🧪 Running Tests Locally

Run:

mvn clean test


This ensures:

Entities map correctly to tables

Controller endpoints respond correctly

Security filters and JWT validation work as expected

🚀 CI/CD with GitHub Actions

Your workflow file: .github/workflows/maven.yml
Automatically does the following:

Builds your project using Maven

Runs all tests automatically

Ensures build consistency on all PRs and pushes

You can view results under the Actions tab in your GitHub repo.

🧰 Developer Notes & Tips

Default Admin is created automatically on startup:

Email: admin@darum.com
Password: ChangeMe123!


Default port: 8080

Kafka: localhost:9092

Zookeeper: localhost:2181

PostgreSQL: localhost:5432

⚠️ Kafka must be running before performing employee CRUD operations.
This is because every create, update, or delete operation on an employee is sent as a Kafka event to notify other services asynchronously.
If Kafka is down, the system cannot dispatch these events, which may cause the operation to fail or the employee data to not be fully propagated to other dependent services.

Manager assignment tip: Always add managers to a department before adding employees. Employees without a department manager will remain unassigned until a manager is added.

Testing: Use mvn test to run unit and integration tests locally.
