# Run the application
mvn spring-boot:run

# Access points
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health
- API Docs: http://localhost:8080/api-docs

# Test immutability features
curl -X GET http://localhost:8080/api/demo/record-vs-lombok
curl -X GET http://localhost:8080/api/demo/builder-pattern

# CRUD operations
curl -X GET http://localhost:8080/api/users
curl -X GET http://localhost:8080/api/employees

# Create user with builder pattern
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Wilson",
    "email": "bob@example.com", 
    "age": 28,
    "birthDate": "1995-06-15",
    "roles": ["USER", "MODERATOR"],
    "preferences": {
      "theme": "dark",
      "language": "en", 
      "emailNotifications": true
    }
  }'

# Update user (demonstrates immutability)
curl -X PUT http://localhost:8080/api/users/bob_wilson \
  -H "Content-Type: application/json" \
  -d '{"age": 29, "preferences": {"theme": "light", "language": "es", "emailNotifications": false}}'

# Role management
curl -X PUT http://localhost:8080/api/users/bob_wilson/roles \
  -H "Content-Type: application/json" \
  -d '{"action": "add", "role": "ADMIN"}'

# Filter by role
curl -X GET http://localhost:8080/api/users/roles/ADMIN