# Stage 1: Build the application using Maven Wrapper
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Maven Wrapper and project files
COPY . .

# Ensure mvnw is executable
RUN chmod +x mvnw

# Build the application (skip tests)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the built JAR
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/biztrack-0.0.1-SNAPSHOT.jar app.jar

# Expose the port used by Spring Boot
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
