# CRUD Backend (Spring Boot) with Authentication & Authorization

## Features
- **JWT-based Authentication**: Secure, stateless authentication using JSON Web Tokens
- **Role-Based Access Control (RBAC)**: Three user roles (USER, ADMIN, MODERATOR)
- **Password Encryption**: BCrypt password hashing
- **CORS Configuration**: Cross-origin resource sharing support
- **Method-Level Security**: Fine-grained access control using annotations
- **Production Ready**: Configurable for both local and production environments

## Requirements
- Java 21
- Maven
- PostgreSQL (or Docker)

## Running Locally
1. Configure PostgreSQL with:
   - Database: `cruddb`
   - User: `cruduser`
   - Password: `crudpass`
2. Update `src/main/resources/application.properties` if needed.
3. Build and run:
   ```sh
   ./mvnw spring-boot:run
   ```

## Running with Docker
1. Build the image:
   ```sh
   docker build -t crud-backend .
   ```
2. Run with Docker Compose (see root-level docker-compose.yml for full stack).

## Authentication

### Default Users
The application automatically creates these default users on startup:
- **admin** / admin123 (ADMIN role)
- **user** / user123 (USER role)  
- **moderator** / moderator123 (MODERATOR role)

### Authentication Endpoints
- `POST   /api/auth/register` - Register new user
- `POST   /api/auth/login`    - Login and get JWT token

### Protected API Endpoints
All book endpoints now require authentication. Include JWT token in Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

- `GET    /api/books`         - List all books (USER, ADMIN, MODERATOR)
- `GET    /api/books/{id}`    - Get book by ID (USER, ADMIN, MODERATOR)
- `POST   /api/books`         - Create new book (ADMIN, MODERATOR)
- `PUT    /api/books/{id}`    - Update book (ADMIN, MODERATOR)
- `DELETE /api/books/{id}`    - Delete book (ADMIN only)

## Testing Authentication

### 1. Login to get JWT token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user123"
  }'
```

### 2. Use token to access protected endpoints
```bash
# Replace <your-jwt-token> with the token from login response
curl -X GET http://localhost:8080/api/books \
  -H "Authorization: Bearer <your-jwt-token>"
```

### 3. Register new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123"
  }'
```

## API Examples (with Authentication)

# API for adding book (requires ADMIN or MODERATOR role)
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "title": "The Pragmatic Programmer",
    "author": "Andrew Hunt"
  }'

# API for getting list of books (requires authentication)
curl -X GET http://localhost:8080/api/books \
  -H "Authorization: Bearer <your-jwt-token>"

# API for deleting books (requires ADMIN role)
curl -X DELETE http://localhost:8080/api/books/3 \
  -H "Authorization: Bearer <your-jwt-token>"

# API for updating books (requires ADMIN or MODERATOR role)
curl -X PUT http://localhost:8080/api/books/BOOK_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "title": "Updated Title",
    "author": "Updated Author"
  }'

###
Command + j for terminal

######### Dockerization
docker build -t crud-backend .
docker run -p 8080:8080 crud-backend
--run in background
docker run -d -p 8080:8080 crud-backend
docker logs <container_id>
docker ps
docker rm <container_id>
docker stop <container_id>

########## DB Dockerization
docker run -d \
  --name postgres-db \
  -e POSTGRES_DB=cruddb \
  -e POSTGRES_USER=cruduser \
  -e POSTGRES_PASSWORD=crudpass \
  -p 5432:5432 \
  -v pg_data:/Users/jignesh/crud-backend/data \
  postgres:16

    -v /absolute/path/on/host:/var/lib/postgresql/data
--Removing data
docker volume rm pg_data

### Docker compose
docker compose up --build

## Accessing App
Backend: http://localhost:8080
PostgreSQL: localhost:5432 (user: cruduser, pass: crudpass, db: cruddb)

## Preparing docker image to push in docker hub
./mvnw clean package -DskipTests
docker build -t jignesh4w/crud-backend:latest .
docker login
docker tag crud-backend jignesh4w/crud-backend:latest
docker push jignesh4w/crud-backend:latest

## Kubernets support
brew install minikube
minikube version
minikube start
minikube start --extra-config=apiserver.service-node-port-range=8000-32767
   kubectl apply -f k8s-postgres-secret.yaml
   kubectl apply -f k8s-postgres-deployment.yaml
   kubectl apply -f k8s-postgres-service.yaml
   kubectl apply -f k8s-crud-backend-deployment.yaml
   kubectl apply -f k8s-crud-backend-service.yaml
   kubectl rollout restart deployment crud-backend
minikube service crud-backend-service --url

   kubectl apply -f k8s-postgres-secret.yaml
   kubectl apply -f k8s-postgres-deployment.yaml
   kubectl apply -f k8s-crud-backend-deployment.yaml

kubectl get all
kubectl get pods
kubectl get deployments
kubectl get services
kubectl logs <pod-name>

## Security Features

- **JWT Token Security**: Tokens are signed with HMAC-SHA256 and have configurable expiration
- **Password Security**: BCrypt password hashing with salt
- **Role-Based Access Control**: Fine-grained permissions based on user roles
- **CORS Configuration**: Secure cross-origin resource sharing
- **Input Validation**: Comprehensive validation of all inputs
- **Error Handling**: Secure error messages that don't leak sensitive information

## Production Deployment

For production deployment, make sure to:
1. Change the JWT secret in `application.properties`
2. Use environment variables for sensitive configuration
3. Enable HTTPS
4. Configure proper CORS origins
5. Use the production profile: `--spring.profiles.active=prod`

See `AUTHENTICATION_GUIDE.md` for detailed security documentation.

## Notes
- Validation and error handling are implemented.
- Uses PostgreSQL as the database.
- Authentication and authorization are now required for all book operations. 