# 🧑‍💼 Employee Management System

## 📘 Overview
The **Employee Management System** is a **Spring Boot–based application** designed to manage employee data, departments, and hierarchical relationships between **administrators, managers, and employees**.

Features include:  
- **Role-based authentication** using JWT tokens  
- **CRUD operations** for employees, managers, and departments  
- **Kafka-based asynchronous messaging** for scalability  
- **PostgreSQL database integration**  
- **Swagger UI** for API documentation  
- **GitHub Actions workflow** for automated CI/CD  

The project follows a **clean modular architecture**, with clearly separated **services, repositories, and entities**.

---

## 🛠️ Technologies Used

| Category        | Technology                        |
|-----------------|----------------------------------|
| Backend Framework | Spring Boot                     |
| Security        | Spring Security + JWT            |
| Database        | PostgreSQL                        |
| ORM             | Hibernate / JPA                   |
| Messaging       | Apache Kafka                      |
| Testing         | JUnit, Mockito, Spring Boot Test  |
| API Docs        | Swagger / Springdoc OpenAPI       |
| Build Tool      | Maven                             |
| CI/CD           | GitHub Actions                    |
| Version Control | Git & GitHub                      |

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd <repository-folder>
```

### 2. Set Up Your Database and Docker
Update your PostgreSQL credentials in `application.properties`, `application.yml`

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=yourpassword
POSTGRES_DB=employeedb
```

Example `application.yml`:
```yaml
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
```

### 3. Run Docker (Zookeeper and Kafka)
To ensure Kafka can send and receive messages properly:  
```bash
docker-compose up -d
docker ps   # Verify Zookeeper and Kafka containers are running
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

---

## 🔑 Default Admin Login
POST: `http://localhost:8080/api/auth/login`  

```json
{
  "email": "admin@darum.com",
  "password": "ChangeMe123!"
}
```

---

## 🏢 Department, Manager, and Employee Operations

### Create Department (Admin only)
POST: `http://localhost:8080/api/admin/departments`  
```json
{
  "name": "Engineering",
  "description": "Handles software development and IT projects"
}
```

### Create Manager (Admin only)
POST: `http://localhost:8080/api/admin/managers`  
```json
{
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice.johnson@example.com",
  "password": "SecurePass123!",
  "phoneNumber": "08012345678",
  "dateOfBirth": "1985-03-15",
  "gender": "FEMALE",
  "position": "Team Lead",
  "employmentDate": "2023-01-10",
  "employmentType": "Full-time",
  "salary": "950000",
  "address": "Lagos"
}
```

### Create Employee (Admin only)
POST: `http://localhost:8080/api/admin/employees`  
```json
{
  "firstName": "Bob",
  "lastName": "Smith",
  "email": "bob.smith@example.com",
  "password": "EmployeePass123!",
  "phoneNumber": "08087654321",
  "dateOfBirth": "1990-06-20",
  "gender": "MALE",
  "position": "Software Developer",
  "employmentType": "Full-time",
  "salary": "450000",
  "address": "Lagos"
}
```

### Assign Manager to Department (Admin only)
POST: `http://localhost:8080/api/admin/departments/{departmentID}/managers/{managerID}`

### Assign Employee to Department (Admin only)
POST: `http://localhost:8080/api/admin/departments/{departmentID}/employees/{employeeID}`

---

## 👤 Get Profiles

### Manager Profile
- Admin: `GET /api/admin/managers/{managerID}`

###  get Employees in Department
- Manager: `GET /api/manager/department/employees`


### Employee Profile
- Admin: `GET /api/admin/employees/{employeeID}`
- Employee: `GET /api/employee/me`

---

## 🔐 Security & Authentication
- JWT-based authentication via `SecurityConfig.java`
- Endpoints are protected based on user roles:

| Role     | Access                                 |
|----------|---------------------------------------|
| ADMIN    | Full access to all endpoints           |
| MANAGER  | View employees in their department    |
| EMPLOYEE | View their own details only            |

Include JWT token in requests:
```
Authorization: Bearer <token>
```

---

## 📄 Swagger API Documentation
Open after running the app:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  

Authorize Swagger with a valid JWT token to access protected endpoints.

---

## 💡 Implementation Tips
1. **Assign Manager Before Employees**  
   Employees require a manager in their department; otherwise, they remain unassigned.

2. **Auto-Manager Assignment**  
   When a new manager is added, existing unassigned employees in the department are linked automatically.

3. **Testing**  
```bash
mvn test
```
   Unit tests automatically spin up an in-memory PostgreSQL container.

4. **CI/CD Integration**  
   Every push or PR triggers:
   - Maven build & tests
   - Logs visible under GitHub Actions tab

---

## ⚠️ Important Notes
- Kafka must be running for employee CRUD operations.
- Default Admin:
  - Email: `admin@darum.com`
  - Password: `ChangeMe123!`
- Default ports:  
  - App: 8080  
  - Kafka: 9092  
  - Zookeeper: 2181  
  - PostgreSQL: 5432
```

---

If you want, I can also **add a table of example API endpoints with their methods and required roles**, which makes it very easy for developers to quickly reference the API.  

Do you want me to do that?
