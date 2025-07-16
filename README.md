# CRUD Backend (Spring Boot)

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

## API Endpoints
- `GET    /api/books`         - List all books
- `GET    /api/books/{id}`    - Get book by ID
- `POST   /api/books`         - Create new book
- `PUT    /api/books/{id}`    - Update book
- `DELETE /api/books/{id}`    - Delete book

# API for adding book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Pragmatic Programmer",
    "author": "Andrew Hunt"
  }'
# API for getting list of books
http://localhost:8080/api/books

# API for deleting books
curl -X DELETE http://localhost:8080/api/books/3

# API for updating books
curl -X PUT http://localhost:8080/api/books/BOOK_ID \
  -H "Content-Type: application/json" \
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

## Notes
- Validation and error handling are implemented.
- Uses PostgreSQL as the database. 