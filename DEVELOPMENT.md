# ReferExpert Development Guide

## Project Structure

### Backend (Java)

```
src/main/java/com/referexpert/referexpert/
├── ReferexpertApplication.java    # Application entry point
├── configuration/                 # App configurations
├── controller/                    # REST endpoints
├── service/                      # Business logic
├── repository/                   # Database access
├── beans/                       # Data models
├── security/                    # JWT and auth
└── util/                       # Utility classes
```

### Key Components

1. **Security (`security/`)**
   - JWT-based authentication
   - Token refresh mechanism
   - Role-based access control

2. **Controllers (`controller/`)**
   - `AppointmentController` - Appointment management
   - `AvailabilityController` - Doctor availability
   - `JwtAuthenticationController` - Auth endpoints
   - `RegistrationController` - User registration
   - `SupportController` - Support tickets
   - `UserController` - User management

3. **Services (`service/`)**
   - `EmailSenderService` - Email notifications
   - `JwtUserDetailsService` - User authentication
   - `MySQLService` - Database operations

4. **Database Migrations (`resources/db/migration/`)**
   - Flyway migration files
   - Version controlled schema updates

## Development Workflow

### 1. Database Changes

1. Create new migration file:
   - Name format: `V{version}__{description}.sql`
   - Place in `src/main/resources/db/migration/`
   - Never modify existing migration files

2. Example migration:
```sql
-- V1_9__add_user_preferences.sql
ALTER TABLE refer_user
ADD COLUMN preferences JSON NULL;
```

### 2. Adding New Features

1. **Create Model (Bean)**
   - Add class in `beans/` package
   - Include proper validation annotations
   - Implement necessary interfaces
   - Add comprehensive JavaDoc

2. **Repository Layer**
   - Create interface extending `BaseRepository<T, ID>`
   - Add specific query methods in interface
   - Implement concrete repository class
   - Add queries to `QueryConstants.java`
   - Include comprehensive unit tests

3. **Service Layer**
   - Create service interface
   - Implement service class
   - Add proper transaction management
   - Include comprehensive error handling
   - Add detailed logging
   - Write thorough unit tests

4. **REST Endpoint**
   - Create controller with proper REST mappings
   - Include security annotations
   - Add input validation
   - Implement proper error responses
   - Include Swagger documentation
   - Write controller unit tests
   - Add integration tests

### 3. Testing Strategy

1. **Unit Testing**
   - Write comprehensive unit tests for all layers
   - Use proper mocking with Mockito
   - Include both success and failure scenarios
   - Test security constraints
   - Maintain high code coverage

2. **Integration Testing**
   - Test end-to-end flows
   - Include database integration tests
   - Test security configurations
   - Verify JWT token flows
   - Test rate limiting functionality

### 4. Security Considerations

1. **Authentication & Authorization**
   - Use `@Secured` or `@PreAuthorize` annotations for role-based access
   - Implement proper JWT token validation
   - Use secure token refresh mechanism
   - Implement token rotation
   - Use refresh tokens with appropriate expiration
   - Add rate limiting for sensitive endpoints
   - Implement brute force protection
   - Add IP-based rate limiting

2. **Data Validation & Sanitization**
   - Use bean validation annotations (@Valid, @NotNull, etc.)
   - Implement input sanitization for all user inputs
   - Use parameterized queries/prepared statements
   - Handle validation errors in global exception handlers
   - Validate file uploads and content types

3. **Security Headers & CORS**
   - Configure security headers (X-Frame-Options, CSP, etc.)
   - Implement proper CORS configuration
   - Enable CSRF protection for non-GET requests
   - Use secure cookie attributes
   - Configure Content Security Policy (CSP)

4. **Error Handling & Logging**
   - Use appropriate HTTP status codes
   - Return consistent error responses without sensitive info
   - Implement comprehensive security logging
   - Monitor and alert on security events
   - Use secure logging practices (no sensitive data)

## Testing

1. **Unit Tests**
   - Test each component in isolation
   - Mock dependencies
   - Focus on edge cases

2. **Integration Tests**
   - Test component interactions
   - Use test database
   - Test complete workflows

3. **API Tests**
   - Test REST endpoints
   - Verify security
   - Check response formats

## Best Practices

1. **Code Style**
   - Follow Java naming conventions
   - Use consistent formatting
   - Write meaningful comments

2. **Error Handling**
   - Use custom exceptions
   - Proper error messages
   - Consistent error response format

3. **Logging**
   - Use appropriate log levels
   - Include relevant context
   - Don't log sensitive data

4. **Security**
   - Validate all input
   - Escape output
   - Use prepared statements
   - Keep dependencies updated

## Common Development Tasks

### Adding a New API Endpoint

1. Create DTO in `beans/` if needed
2. Add business logic in service layer
3. Create controller method with proper annotations
4. Add security constraints
5. Document the API
6. Add tests

### Database Schema Updates

1. Create new Flyway migration
2. Update model classes
3. Update repository queries
4. Add data migration if needed
5. Test migration
6. Update tests

### Security Updates

1. Review security configuration
2. Update JWT settings if needed
3. Check role-based access
4. Verify token handling
5. Test security measures

## API Documentation

### REST Endpoints

#### Authentication
- `POST /api/auth/login` - User login
  - Request: `{ "email": string, "password": string }`
  - Response: `{ "token": string, "refreshToken": string }`

- `POST /api/auth/refresh` - Refresh access token
  - Request: `{ "refreshToken": string }`
  - Response: `{ "token": string }`

#### User Management
- `POST /api/users` - Create new user
- `GET /api/users/{id}` - Get user details
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

#### Appointments
- `POST /api/appointments` - Create appointment
- `GET /api/appointments/{id}` - Get appointment details
- `PUT /api/appointments/{id}` - Update appointment
- `GET /api/appointments/user/{userId}` - List user appointments

#### Referrals
- `POST /api/referrals` - Create referral
- `GET /api/referrals/{id}` - Get referral details
- `PUT /api/referrals/{id}` - Update referral
- `GET /api/referrals/doctor/{doctorId}` - List doctor's referrals

### Response Formats

#### Success Response
```json
{
    "status": "success",
    "data": {
        // Response data
    }
}
```

#### Error Response
```json
{
    "status": "error",
    "error": {
        "code": "ERROR_CODE",
        "message": "Error description"
    }
}
```

### Authentication

All protected endpoints require a valid JWT token in the Authorization header:
```
Authorization: Bearer <token>
```

### Rate Limiting

- Standard endpoints: 100 requests per hour per IP
- Authentication endpoints: 10 requests per 5 minutes per IP
- Password reset: 3 attempts per hour

### Request/Response Examples

#### Create Appointment
Request:
```json
POST /api/appointments
{
    "patientId": "123",
    "doctorId": "456",
    "date": "2024-02-01T10:00:00Z",
    "type": "INITIAL_CONSULTATION",
    "notes": "Initial visit for knee pain"
}
```

Response:
```json
{
    "status": "success",
    "data": {
        "id": "789",
        "patientId": "123",
        "doctorId": "456",
        "date": "2024-02-01T10:00:00Z",
        "type": "INITIAL_CONSULTATION",
        "notes": "Initial visit for knee pain",
        "status": "SCHEDULED"
    }
}
```
