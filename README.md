# ReferExpert

A comprehensive medical referral management system that facilitates communication between healthcare providers and manages patient referrals efficiently.

## Project Structure

The project is divided into two main components:

### Backend (referexpert/)
- Java Spring Boot application (v2.2.11)
- REST API endpoints
- MySQL database integration
- Email notification system
- Database migrations using Flyway
- JWT authentication

### Frontend (referexpert-frontend/)
- React-based single-page application
- Material-UI components
- Real-time notifications
- Responsive design
- Patient and doctor portals

## Features

- User authentication and authorization
- Patient referral management
- Appointment scheduling and tracking
- Real-time notifications
- Doctor availability management
- Support contact system
- User profile management
- Secure password reset functionality

## Prerequisites

- Java 8 or higher
- Eclipse IDE
- MySQL 5.7 or higher
- Node.js and npm (for frontend)
- Gmail account (for email notifications)

## Setup Instructions

### Database Setup

1. Install MySQL and run the following commands:

```sql
CREATE DATABASE referexpert;

CREATE USER 'referexpert'@'localhost' IDENTIFIED WITH mysql_native_password BY 'MySQL*987';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,REFERENCES,ALTER,INDEX on referexpert.* TO 'referexpert'@'localhost';
FLUSH PRIVILEGES;
```

### Backend Setup

1. Clone the repository
2. Open the project in Eclipse IDE
3. Update `src/main/resources/application.properties` with your database and email credentials
4. Run `ReferexpertApplication.java` as Java Application
5. The application will create required tables and load initial data automatically

### Frontend Setup

1. Navigate to the `referexpert-frontend` directory
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```
4. Access the application at `http://localhost:3000`

## API Documentation

The backend provides RESTful APIs for:
- User management
- Appointments
- Referrals
- Notifications
- Support contacts

## Tech Stack

### Backend
- Spring Boot 2.2.11
- Spring Security with JWT and refresh tokens
- MySQL Database with connection pooling
- Flyway Migration for database versioning
- Java 8
- Maven for dependency management
- JUnit 5 for testing
- Mockito for mocking
- SLF4J/Log4j2 for logging
- Spring AOP for cross-cutting concerns

### Frontend
- React 17
- Material-UI 4
- React Router for navigation
- Modern JavaScript (ES6+)
- Jest for testing
- Axios for API calls
- Redux for state management

### DevOps & Security
- HTTPS/TLS encryption
- Rate limiting
- Brute force protection
- IP-based security
- Content Security Policy (CSP)
- CORS configuration
- Security headers
- CI/CD pipeline integration
- CSS3

## Development

The project uses a modular architecture with:
- Flyway for database versioning
- JWT for secure authentication
- Material-UI for consistent design
- Responsive layouts for mobile support

## Security Features

### Authentication & Authorization
- JWT-based authentication with refresh tokens
- Role-based access control (RBAC)
- Secure password hashing with BCrypt
- Token rotation and expiration
- Rate limiting on auth endpoints

### Data Security
- Input validation and sanitization
- Parameterized SQL queries
- XSS protection
- CSRF protection
- Secure session management

### Infrastructure Security
- TLS/SSL encryption
- Security headers configuration
- CORS policy enforcement
- Content Security Policy (CSP)
- Brute force protection

### Compliance & Logging
- GDPR-compliant data handling
- Secure audit logging
- Security event monitoring
- Data encryption at rest
- Regular security updates

## Security Requirements

1. **Environment Setup**
   - SSL certificate
   - Secure key storage
   - Environment variables configuration
   - Firewall rules

2. **Production Deployment**
   - HTTPS enforcement
   - Secure cookie configuration
   - Rate limiting setup
   - Monitoring configuration