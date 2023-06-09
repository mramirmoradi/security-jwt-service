# Security JWT service (Authentication Service)
Spring boot module as security jwt service.

## Tech Stack
- **JAVA** = version 17
- **Spring boot framework** = 3.0.6

## APIs
Login API: http://localhost:8080/api/login <br>
Signup API: http://localhost:8080/api/signup <br>
Swagger Doc API: http://localhost:8080/api/swagger-ui.html

## Deployment
By the above command, ecr-service will deploy on: <link> http://localhost:8080/api </link>

```bash
  docker-compose up --build
```
## Run tests
```bash
    mvn test
```
## Run Locally
Go to the project directory and install dependencies.

```bash
  ./mvnw clean pacakge
```

### Environment Variables
To run this project without docker locally, you will need to add your local machine environment variables to application-local.properties file and dont forget to change the profile.

### Postman collection
You can use postman collection that exist on project base dir.