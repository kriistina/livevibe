# Step 1: Build with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Step 2: Run with OpenJDK only (cleaner, smaller image)
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/LiveVibe-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]

