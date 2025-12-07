# שלב 1: בנייה באמצעות תמונה רשמית של Maven (חוסך בעיות mvnw)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# העתקת קבצי הפרויקט
COPY . .

# בניית הפרויקט באמצעות הפקודה הגלובלית mvn (ולא ./mvnw)
RUN mvn clean package -DskipTests

# שלב 2: יצירת סביבת ההרצה הקלה
FROM eclipse-temurin:17-jre
WORKDIR /app

# העתקת ה-JAR שנוצר בשלב הראשון
COPY --from=build /app/target/*.jar app.jar

# חשיפת הפורט
EXPOSE 8080

# הפעלת האפליקציה
ENTRYPOINT ["java", "-jar", "app.jar"]