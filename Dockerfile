# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package

# Stage 2: Run the application
FROM openjdk:17.0.2-jdk-oracle
WORKDIR /app
COPY --from=build /app/target/security-jwt-0.0.1-SNAPSHOT.jar ./run.jar
CMD ["java", "-jar", "run.jar"]