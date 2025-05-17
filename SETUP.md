# ReferExpert Setup Guide

## Prerequisites

- Java 8 or higher
- Eclipse IDE
- MySQL 5.7 or higher
- Node.js and npm (for frontend)
- Gmail account (for email notifications)

## Configuration Files

### 1. Database Configuration

Create a MySQL database and user with the following commands:

```sql
CREATE DATABASE referexpert;

CREATE USER 'referexpert'@'localhost' IDENTIFIED WITH mysql_native_password BY 'MySQL*987';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,REFERENCES,ALTER,INDEX on referexpert.* TO 'referexpert'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Application Properties

Configure `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8081
application.url=http://localhost:8081/
referexpert.home.url=http://localhost:3000

# Database Configuration
mysql.datasource.url=jdbc:mysql://localhost:3306/referexpert?max_connections=50&allowMultiQueries=true&useSSL=false
mysql.datasource.user=referexpert
mysql.datasource.password=MySQL*987

# Email Configuration (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<your-gmail>
spring.mail.password=<your-app-specific-password>
spring.mail.replyto=<your-reply-email>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# JWT Configuration
jwt.secret=${JWT_SECRET_KEY}  # Use environment variable
jwt.access.expiration.ms=3600000  # 1 hour
jwt.refresh.expiration.ms=86400000  # 24 hours
jwt.token.issuer=referexpert
jwt.token.audience=referexpert-api

# Security Configuration
security.require-ssl=true
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=strict
server.servlet.session.timeout=1800  # 30 minutes session timeout
security.headers.hsts=true
security.headers.frame-options=DENY
security.headers.xss-protection=1; mode=block
security.headers.content-type-options=nosniff
security.headers.referrer-policy=strict-origin-when-cross-origin
security.headers.content-security-policy=default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline';

# Rate Limiting Configuration
security.rate-limit.enabled=true
security.rate-limit.max-requests=100
security.rate-limit.time-window=3600000  # 1 hour per IP
security.rate-limit.auth-max-requests=10  # Stricter limit for auth endpoints
security.rate-limit.auth-time-window=300000  # 5 minutes for auth endpoints
security.rate-limit.password-reset-max=3  # Password reset attempts per hour
security.rate-limit.ip-whitelist=127.0.0.1,::1  # Local development IPs

# Brute Force Protection
security.brute-force.max-attempts=5
security.brute-force.block-duration=900000  # 15 minutes
security.brute-force.reset-after=3600000  # Reset attempts after 1 hour

# Audit Logging
audit.enabled=true
audit.log-location=logs/audit.log
audit.include-request-body=false  # Don't log sensitive data
audit.include-response-body=false
```

## Installation Steps

### Backend Setup

1. Clone the repository
2. Import project into Eclipse as Maven project
3. Update application.properties with your configuration
4. Run the following commands for database setup:
```powershell
# Create database and user
mysql -u root -p
```
Then in MySQL console:
```sql
CREATE DATABASE referexpert;
CREATE USER 'referexpert'@'localhost' IDENTIFIED WITH mysql_native_password BY 'MySQL*987';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,REFERENCES,ALTER,INDEX on referexpert.* TO 'referexpert'@'localhost';
FLUSH PRIVILEGES;
```

5. Run tests to verify setup:
```powershell
mvn clean test
```

6. Start the application:
```powershell
mvn spring-boot:run
```
   - This will automatically execute Flyway migrations
   - Create required database tables
   - Initialize default user types and specialties

7. Verify the application:
   - Check http://localhost:8081/health
   - Review logs in `logs/application.log`
   - Check audit logs in `logs/audit.log`

### Frontend Setup

1. Navigate to referexpert-frontend directory:
```powershell
cd referexpert-frontend
```

2. Install dependencies:
```powershell
npm install
```

3. Run tests:
```powershell
npm test
```

4. Start development server:
```powershell
npm start
```

5. Build for production:
```powershell
npm run build
```

### Testing Environment Setup

1. Install test dependencies:
```powershell
mvn install -P test
```

2. Run specific test suites:
```powershell
# Run unit tests only
mvn test

# Run integration tests
mvn verify -P integration-test

# Run with coverage report
mvn verify -P coverage
```

3. View test results:
   - Unit test results: `target/surefire-reports`
   - Integration test results: `target/failsafe-reports`
   - Coverage reports: `target/site/jacoco`
4. Access application at http://localhost:3000

## Verify Installation

1. Backend Health Check
   - Access: http://localhost:8081/referexpert/health
   - Should return: {"status": "UP"}

2. Database Verification
   - Check if Flyway migrations completed successfully
   - Verify tables are created in the referexpert database

3. Frontend Connection
   - Open browser to http://localhost:3000
   - Should see the login page
   - Try registering a new user

## Common Issues & Troubleshooting

1. Database Connection
   - Verify MySQL is running
   - Check credentials in application.properties
   - Ensure database and user exist

2. Email Configuration
   - For Gmail, use App-Specific Password
   - Enable "Less secure app access" in Gmail
   - Test email configuration with registration flow

3. Frontend Connection Issues
   - Check if backend is running on port 8081
   - Verify CORS configuration
   - Clear browser cache if needed

## Troubleshooting Guide

### Common Issues and Solutions

#### Database Connection Issues

1. **Connection Refused**
```
Error: Communications link failure ... Connection refused
```
Solutions:
- Check if MySQL is running: `sudo systemctl status mysql`
- Verify database credentials in application.properties
- Check firewall rules: `sudo ufw status`
- Verify MySQL port is open: `netstat -tlnp | grep mysql`

2. **Access Denied**
```
Error: Access denied for user 'referexpert'@'localhost'
```
Solutions:
- Verify user exists: `mysql -u root -p -e "SELECT User,Host FROM mysql.user;"`
- Check grants: `SHOW GRANTS FOR 'referexpert'@'localhost';`
- Reset user permissions using setup SQL commands

#### Application Startup Issues

1. **Port Already in Use**
```
Error: Web server failed to start. Port 8081 was already in use.
```
Solutions:
- Find process: `lsof -i :8081`
- Kill process: `kill -9 <PID>`
- Change port in application.properties

2. **Out of Memory**
```
Error: java.lang.OutOfMemoryError: Java heap space
```
Solutions:
- Increase heap size: Add `-Xmx512m` to JVM options
- Check memory leaks using JProfiler
- Review memory-intensive operations

#### Authentication Issues

1. **Invalid JWT Token**
```
Error: Invalid JWT token
```
Solutions:
- Check token expiration
- Verify JWT secret key is properly set
- Clear browser cookies and local storage
- Check if times are synced between servers

2. **CORS Errors**
```
Error: Access to XMLHttpRequest has been blocked by CORS policy
```
Solutions:
- Check CORS configuration in SecurityConfig
- Verify allowed origins
- Check Nginx proxy headers

#### Frontend Issues

1. **API Connection Failed**
```
Error: Failed to fetch API endpoint
```
Solutions:
- Check API URL configuration
- Verify network connectivity
- Check browser console for CORS errors
- Verify SSL certificate validity

2. **White Screen After Deployment**
```
Error: Blank page or React app not loading
```
Solutions:
- Check browser console for JavaScript errors
- Verify build files are properly deployed
- Check Nginx configuration
- Clear browser cache

### Diagnostic Tools

1. **Application Logs**
```bash
# View application logs
tail -f /var/log/referexpert/application.log

# View error logs
grep ERROR /var/log/referexpert/application.log
```

2. **Database Diagnostics**
```sql
-- Check database status
SHOW ENGINE INNODB STATUS;

-- Check slow queries
SHOW FULL PROCESSLIST;
```

3. **Memory Usage**
```bash
# Check Java process memory
jmap -heap <PID>

# Monitor system resources
top -p <PID>
```

4. **Network Connectivity**
```bash
# Test database connection
telnet localhost 3306

# Check HTTP endpoints
curl -v https://your-domain.com/api/health
```

### Performance Issues

1. **Slow API Responses**
Solutions:
- Enable debug logging temporarily
- Check database query performance
- Review connection pool settings
- Monitor system resources
- Enable and analyze Spring Boot Actuator metrics

2. **High Memory Usage**
Solutions:
- Review heap dump: `jmap -dump:format=b,file=heap.bin <PID>`
- Analyze with Java VisualVM
- Check for memory leaks
- Review caching strategies

### Security Issues

1. **Failed Login Attempts**
Solutions:
- Review auth logs
- Check rate limiting configuration
- Verify IP whitelist
- Enable additional security logging

2. **SSL Certificate Issues**
Solutions:
- Verify certificate validity
- Check certificate chain
- Review SSL configuration
- Update SSL settings in Nginx

### System Health Checks

1. **Application Health**
```bash
# Check application status
curl -k https://localhost:8081/actuator/health

# View metrics
curl -k https://localhost:8081/actuator/metrics
```

2. **Database Health**
```sql
-- Check database status
SELECT VERSION();
SHOW STATUS;
SHOW VARIABLES;
```

3. **System Resources**
```bash
# Check disk space
df -h

# Check memory usage
free -m

# Check load average
uptime
```

## Security Setup

### 1. SSL/TLS Configuration
1. Generate or obtain SSL certificate
2. Configure SSL in application.properties
3. Enable HTTPS redirect

### 2. JWT Security
1. Generate a strong secret key:
```bash
openssl rand -base64 64
```
2. Set the JWT_SECRET_KEY environment variable with the generated key
3. Configure token expiration times based on security requirements
4. Implement token refresh mechanism

### 3. Security Headers
The application automatically configures the following security headers:
- X-Content-Type-Options: nosniff
- X-Frame-Options: DENY
- X-XSS-Protection: 1; mode=block
- Strict-Transport-Security: max-age=31536000
- Content-Security-Policy: default-src 'self'
- Cache-Control: no-cache, no-store

### 4. Rate Limiting
Rate limiting is configured to prevent brute force attacks:
- Default: 100 requests per hour per IP
- Customize in application.properties

### 5. Environment Variables
Required security-related environment variables:
- JWT_SECRET_KEY: JWT signing key
- SPRING_MAIL_PASSWORD: Email service password
- MYSQL_PASSWORD: Database password

## Deployment Guide

### Prerequisites
- Linux server with Java 8 or higher
- MySQL 5.7 or higher
- Nginx web server
- SSL certificate
- Domain name configured

### Production Environment Setup

1. **Server Preparation**
```bash
# Update system
sudo apt update && sudo apt upgrade

# Install required packages
sudo apt install nginx mysql-server openjdk-8-jdk
```

2. **SSL Configuration**
- Install certbot for SSL
- Generate and configure SSL certificates
- Update Nginx configuration

3. **Database Setup**
- Secure MySQL installation
- Create production database and user
- Configure firewall rules

4. **Application Deployment**

Backend:
```bash
# Create application directory
sudo mkdir -p /opt/referexpert
sudo chown -R ubuntu:ubuntu /opt/referexpert

# Deploy JAR file
scp target/referexpert.jar ubuntu@your-server:/opt/referexpert/

# Create service file
sudo nano /etc/systemd/system/referexpert.service

# Service file content:
[Unit]
Description=ReferExpert Backend
After=network.target

[Service]
Type=simple
User=ubuntu
Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="JWT_SECRET_KEY=your-secret-key"
WorkingDirectory=/opt/referexpert
ExecStart=/usr/bin/java -jar referexpert.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

Frontend:
```bash
# Build frontend
npm run build

# Deploy to Nginx
scp -r build/* ubuntu@your-server:/var/www/referexpert/
```

### Nginx Configuration

```nginx
# /etc/nginx/sites-available/referexpert
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;

    # Frontend
    location / {
        root /var/www/referexpert;
        try_files $uri $uri/ /index.html;

        # Security headers
        add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
        add_header X-Frame-Options "DENY" always;
        add_header X-Content-Type-Options "nosniff" always;
        add_header X-XSS-Protection "1; mode=block" always;
        add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline';" always;
    }

    # Backend API
    location /api {
        proxy_pass http://localhost:8081;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

### Monitoring Setup

1. Install monitoring tools:
```bash
# Install Prometheus and Grafana
sudo apt install prometheus grafana
```

2. Configure monitoring endpoints in application.properties:
```properties
management.endpoints.web.exposure.include=health,metrics,prometheus
```

3. Configure logging:
```bash
# Create log directory
sudo mkdir -p /var/log/referexpert
sudo chown -R ubuntu:ubuntu /var/log/referexpert
```

### Backup Configuration

1. Database backup script:
```bash
#!/bin/bash
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_DIR="/backup/mysql"
mysqldump -u backup_user -p referexpert | gzip > "$BACKUP_DIR/referexpert_$TIMESTAMP.sql.gz"
```

2. Add to crontab:
```bash
0 1 * * * /path/to/backup-script.sh
```

### Post-Deployment Verification

1. Check application status:
```bash
sudo systemctl status referexpert
```

2. Verify logs:
```bash
tail -f /var/log/referexpert/application.log
```

3. Monitor metrics:
- Access Grafana dashboard
- Check Prometheus metrics
- Review error rates and response times

### Rollback Procedure

1. Stop application:
```bash
sudo systemctl stop referexpert
```

2. Restore previous version:
```bash
cd /opt/referexpert
mv referexpert.jar referexpert.jar.new
mv referexpert.jar.old referexpert.jar
```

3. Restart application:
```bash
sudo systemctl start referexpert
```

## Environment-Specific Configurations

### Development Environment

```properties
# application-dev.properties
server.port=8081
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
logging.level.root=INFO
logging.level.com.referexpert=DEBUG
security.require-ssl=false
security.rate-limit.enabled=false
```

### Testing Environment

```properties
# application-test.properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.show-sql=true
logging.level.root=DEBUG
security.require-ssl=false
security.rate-limit.enabled=false
```

### Staging Environment

```properties
# application-staging.properties
server.port=8081
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=INFO
security.require-ssl=true
security.rate-limit.enabled=true
security.rate-limit.max-requests=1000
monitoring.enabled=true
```

### Production Environment

```properties
# application-prod.properties
server.port=8081
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
logging.level.com.referexpert=INFO
security.require-ssl=true
security.rate-limit.enabled=true
security.rate-limit.max-requests=100

# Production-specific settings
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10

# Caching configuration
spring.cache.type=redis
spring.redis.host=${REDIS_HOST}
spring.redis.port=${REDIS_PORT}
spring.redis.password=${REDIS_PASSWORD}

# Session configuration
spring.session.store-type=redis
server.servlet.session.timeout=1h

# SSL configuration
server.ssl.key-store=/etc/letsencrypt/live/referexpert.com/keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12

# Monitoring
management.endpoints.web.exposure.include=health,metrics,prometheus
management.endpoint.health.show-details=when_authorized
```

### Environment Variables

Each environment should have its own .env file with appropriate values:

```bash
# .env.development
JWT_SECRET_KEY=dev-secret-key
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=referexpert_dev
MYSQL_USER=referexpert
MYSQL_PASSWORD=dev-password

# .env.production
JWT_SECRET_KEY=${PROD_JWT_SECRET}
MYSQL_HOST=${PROD_DB_HOST}
MYSQL_PORT=${PROD_DB_PORT}
MYSQL_DATABASE=referexpert_prod
MYSQL_USER=${PROD_DB_USER}
MYSQL_PASSWORD=${PROD_DB_PASSWORD}
REDIS_HOST=${PROD_REDIS_HOST}
REDIS_PORT=${PROD_REDIS_PORT}
REDIS_PASSWORD=${PROD_REDIS_PASSWORD}
SSL_KEYSTORE_PASSWORD=${PROD_SSL_PASSWORD}
```
