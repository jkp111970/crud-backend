# Authentication and Authorization Guide

This Spring Boot application now includes a comprehensive JWT-based authentication and authorization system that follows industry best practices.

## Features

- **JWT Token Authentication**: Secure, stateless authentication using JSON Web Tokens
- **Role-Based Access Control (RBAC)**: Three user roles (USER, ADMIN, MODERATOR)
- **Password Encryption**: BCrypt password hashing
- **CORS Configuration**: Cross-origin resource sharing support
- **Method-Level Security**: Fine-grained access control using annotations
- **Production Ready**: Configurable for both local and production environments

## User Roles and Permissions

### USER Role
- Can read books (GET `/api/books` and `/api/books/{id}`)
- Cannot create, update, or delete books

### MODERATOR Role
- Can read books
- Can create and update books
- Cannot delete books

### ADMIN Role
- Full access to all book operations (CRUD)
- Can delete books

## API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "user123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "user",
  "email": "user@example.com",
  "role": "USER"
}
```

### Protected Endpoints

All book endpoints now require authentication. Include the JWT token in the Authorization header:

```http
Authorization: Bearer <your-jwt-token>
```

## Default Users

The application automatically creates these default users on startup:

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| user | user123 | USER |
| moderator | moderator123 | MODERATOR |

## Configuration

### JWT Configuration (application.properties)

```properties
# JWT Configuration
app.jwt.secret=your-super-secret-jwt-key-for-production-change-this-in-production-environment
app.jwt.expiration=86400000
```

### Production Security Checklist

1. **Change JWT Secret**: Replace the default JWT secret with a strong, unique secret
2. **Environment Variables**: Use environment variables for sensitive configuration
3. **HTTPS**: Always use HTTPS in production
4. **Token Expiration**: Consider shorter token expiration times for production
5. **Rate Limiting**: Implement rate limiting for authentication endpoints
6. **Audit Logging**: Enable security audit logging

### Environment-Specific Configuration

#### Local Development
```properties
# Use default JWT secret for development
app.jwt.secret=defaultSecretKeyForDevelopmentOnly
app.jwt.expiration=86400000
```

#### Production
```properties
# Use environment variable for JWT secret
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=3600000
```

## Testing the Authentication

### 1. Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 2. Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user123"
  }'
```

### 3. Use Token to Access Protected Endpoints
```bash
curl -X GET http://localhost:8080/api/books \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Security Features

### JWT Token Security
- Tokens are signed with HMAC-SHA256
- Configurable expiration time
- Stateless authentication (no server-side session storage)

### Password Security
- BCrypt password hashing with salt
- Minimum password length validation
- Secure password storage

### CORS Configuration
- Configurable allowed origins
- Support for credentials
- Proper header handling

### Method-Level Security
- `@PreAuthorize` annotations for fine-grained control
- Role-based access control
- Method-level permission checking

## Error Handling

The application includes comprehensive error handling for authentication scenarios:

- **401 Unauthorized**: Invalid credentials or missing token
- **403 Forbidden**: Insufficient permissions
- **400 Bad Request**: Invalid request data
- **404 Not Found**: User not found

## Monitoring and Health Checks

The application includes Spring Boot Actuator for monitoring:

- Health checks: `/actuator/health`
- Application info: `/actuator/info`
- Metrics: `/actuator/metrics`

## Best Practices Implemented

1. **Principle of Least Privilege**: Users only have access to what they need
2. **Defense in Depth**: Multiple layers of security
3. **Secure by Default**: Sensible defaults that prioritize security
4. **Input Validation**: Comprehensive validation of all inputs
5. **Error Handling**: Secure error messages that don't leak sensitive information
6. **Logging**: Appropriate security event logging
7. **Configuration Management**: Environment-specific configuration

## Troubleshooting

### Common Issues

1. **Token Expired**: Check token expiration time and refresh if needed
2. **Invalid Token**: Ensure token is properly formatted and signed
3. **Access Denied**: Verify user has the required role for the operation
4. **CORS Issues**: Check CORS configuration for frontend integration

### Debug Mode

Enable debug logging for security-related issues:

```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.example.crud_backend=DEBUG
```

## Integration with Frontend

### JavaScript/TypeScript Example
```javascript
// Login
const loginResponse = await fetch('/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    username: 'user',
    password: 'user123'
  })
});

const authData = await loginResponse.json();
const token = authData.token;

// Use token for subsequent requests
const booksResponse = await fetch('/api/books', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

This authentication system provides a solid foundation for securing your Spring Boot application while maintaining flexibility for different deployment scenarios. 