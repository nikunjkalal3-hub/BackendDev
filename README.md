# BackendDev — Spring Boot REST API

A Spring Boot backend application built to practice and demonstrate REST APIs, PostgreSQL, Spring Data JPA, Spring Security, JWT authentication, role-based authorization, validation, exception handling, and automated testing.

## 🚀 Features

- User registration
- User login with JWT authentication
- BCrypt password hashing
- Role-based authorization (`USER` / `ADMIN`)
- Protected REST APIs
- PostgreSQL database integration
- Spring Data JPA
- Request validation
- Global exception handling
- Duplicate email protection
- Swagger / OpenAPI documentation
- Automated unit and security tests
- Clean separation of Controller, Service, Repository, DTO, Entity, and Security layers

## 🛠️ Tech Stack

- Java 26
- Spring Boot 4.1.0
- Spring Security 7.1.0
- Spring Data JPA
- PostgreSQL
- JWT — JJWT 0.12.6
- BCrypt
- Maven
- JUnit 5
- Mockito
- Spring Security Test
- SpringDoc OpenAPI

## 📁 Project Structure

```text
BackendDev/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── backend/backenddev/
    │   │       ├── Controller/
    │   │       ├── Service/
    │   │       ├── Filter/
    │   │       ├── config/
    │   │       ├── dto/
    │   │       ├── entity/
    │   │       ├── exception/
    │   │       └── repository/
    │   │
    │   └── resources/
    │       └── application.properties
    │
    └── test/
        └── java/
            └── backend/backenddev/
```

## 🔐 Authentication Flow

The application uses JWT-based authentication.

### Registration

```text
Client
  ↓
POST /api/auth/register
  ↓
AuthController
  ↓
AuthService
  ↓
Password encoded with BCrypt
  ↓
User saved in PostgreSQL
```

The password is never stored as plain text.

### Login

```text
Client
  ↓
POST /api/auth/login
  ↓
AuthenticationManager
  ↓
CustomUserDetailsService
  ↓
Password verification
  ↓
JwtService
  ↓
JWT token returned
```

The client then sends the token with protected requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

### JWT Request Flow

```text
HTTP Request
     ↓
JwtAuthenticationFilter
     ↓
Read Authorization header
     ↓
Extract JWT
     ↓
Validate JWT
     ↓
Load user details
     ↓
Set SecurityContext
     ↓
Controller
```

## 👥 Authorization

The application currently uses two roles:

- `USER`
- `ADMIN`

### Endpoint permissions

| Endpoint | Method | Access |
|---|---|---|
| `/api/auth/register` | POST | Public |
| `/api/auth/login` | POST | Public |
| `/api/users` | GET | USER / ADMIN |
| `/api/users/{id}` | GET | USER / ADMIN |
| `/api/users` | POST | ADMIN |
| `/api/users/{id}` | PUT | ADMIN |
| `/api/users/{id}` | DELETE | ADMIN |
| Swagger/OpenAPI | GET | Public |

Method-level authorization is implemented with `@PreAuthorize`.

Example:

```java
@PreAuthorize("hasRole('ADMIN')")
```

## 📡 API Endpoints

### Register

```http
POST /api/auth/register
```

Example request:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

Example response:

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Login

```http
POST /api/auth/login
```

Example request:

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

Example response:

```json
{
  "token": "<JWT_TOKEN>"
}
```

### Get All Users

```http
GET /api/users
```

Requires authentication.

### Get User

```http
GET /api/users/{id}
```

Requires `USER` or `ADMIN` role.

### Create User

```http
POST /api/users
```

Requires `ADMIN` role.

### Update User

```http
PUT /api/users/{id}
```

Requires `ADMIN` role.

Example request:

```json
{
  "name": "Updated Name",
  "email": "updated@example.com"
}
```

### Delete User

```http
DELETE /api/users/{id}
```

Requires `ADMIN` role.

## ✅ Validation

The application uses Jakarta Bean Validation.

Examples include:

```java
@NotBlank
@Email
@Size(min = 8)
```

Registration validates:

- Name is required
- Email is required
- Email must have a valid format
- Password is required
- Password must contain at least 8 characters

Login validates:

- Email is required
- Email format is valid
- Password is required

User updates validate:

- Name is required
- Email is required
- Email format is valid

## ❌ Exception Handling

Global exception handling is implemented using `@RestControllerAdvice`.

### User Not Found

Returns:

```http
404 NOT FOUND
```

Example:

```json
{
  "message": "User not found with id: 1"
}
```

### Duplicate Email

Returns:

```http
409 CONFLICT
```

Example:

```json
{
  "message": "Email already exists"
}
```

### Validation Error

Returns:

```http
400 BAD REQUEST
```

Example:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Invalid email format",
    "password": "Password must be at least 8 characters"
  }
}
```

## 🗄️ Database

The application uses PostgreSQL.

Database:

```text
backenddev
```

The application uses Spring Data JPA and Hibernate for database operations.

The `User` entity is mapped to the:

```text
users
```

table.

Passwords are stored using BCrypt hashes rather than plain-text passwords.

## ⚙️ Environment Variables

Sensitive configuration is loaded from environment variables.

Required variables:

```text
DB_PASSWORD
JWT_SECRET
```

Example PowerShell setup:

```powershell
$env:DB_PASSWORD="your_database_password"
$env:JWT_SECRET="your_long_random_secret"
```

Do not commit real credentials or JWT secrets to GitHub.

## ⚙️ Application Configuration

The application runs on:

```text
http://localhost:8086
```

Swagger UI:

```text
http://localhost:8086/swagger-ui.html
```

OpenAPI specification:

```text
http://localhost:8086/v3/api-docs
```

## ▶️ Running the Application

Make sure PostgreSQL is running and the required environment variables are configured.

From the project directory:

```powershell
.\mvnw.cmd spring-boot:run
```

The application will start on:

```text
http://localhost:8086
```

## 🧪 Running Tests

Run the complete test suite:

```powershell
.\mvnw.cmd test
```

The current project contains **43 automated tests**, covering:

- Application startup
- Authentication service
- Authentication controller
- User service
- User controller
- Security authorization
- JWT service
- JWT authentication filter
- Global exception handling

Current result:

```text
43 tests
43 passed
0 failed
```

## 🔒 Security

The application implements several security practices:

- JWT authentication
- BCrypt password hashing
- Role-based authorization
- Protected endpoints
- JWT signature verification
- JWT expiration validation
- Validation of incoming requests
- Global exception handling
- Sensitive configuration through environment variables
- JWTs are not logged by the application

CSRF is disabled because the backend is designed as a stateless REST API using JWT authentication.

## 🧪 Testing Security

The security tests verify access control for protected endpoints.

Examples include:

- Unauthenticated users cannot access protected endpoints.
- `USER` can access permitted read endpoints.
- `ADMIN` can access administrative endpoints.
- Non-admin users cannot create, update, or delete users.

JWT authentication is also tested independently through the JWT service and authentication filter tests.

## 📚 Swagger / OpenAPI

Interactive API documentation is available through Swagger UI:

```text
http://localhost:8086/swagger-ui.html
```

Swagger can be used to:

1. View available API endpoints.
2. Inspect request and response models.
3. Send API requests.
4. Test authenticated endpoints using a JWT.

For protected endpoints, use the Swagger **Authorize** button and provide:

```text
Bearer <JWT_TOKEN>
```

## 🏗️ Architecture

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

Security-related requests additionally pass through:

```text
HTTP Request
    ↓
JWT Filter
    ↓
Spring Security
    ↓
Controller
```

DTOs are used to control the data exposed through the API instead of returning the database entity directly.

## 🔮 Future Improvements

Possible future improvements include:

- Refresh token support
- Logout / token revocation strategy
- More granular ownership-based authorization
- Pagination for user listing
- Search and filtering
- Improved API error standardization
- Production-specific configuration
- Database migrations with Flyway or Liquibase
- Docker support
- CI/CD pipeline
- More integration tests
- Production API documentation configuration
- Deployment to a cloud platform

## 👨‍💻 Author

Built as a hands-on Spring Boot backend project focused on learning and demonstrating:

- Java
- Spring Boot
- REST API development
- Spring Security
- JWT authentication
- PostgreSQL
- JPA/Hibernate
- Validation
- Exception handling
- Automated testing

---

## ⭐ Project Status

The backend is currently functional with:

```text
✅ REST APIs
✅ PostgreSQL
✅ User registration
✅ User login
✅ BCrypt password hashing
✅ JWT authentication
✅ Role-based authorization
✅ Validation
✅ Exception handling
✅ Swagger/OpenAPI
✅ Automated tests
✅ 43/43 tests passing
```