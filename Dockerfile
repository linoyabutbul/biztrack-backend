# שלב 1: בנייה
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# כעת הפקודה תעבוד חלק כי ה-pom.xml תקין
RUN mvn clean package -DskipTests

# שלב 2: הרצה
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]