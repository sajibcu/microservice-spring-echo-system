# Stage 1: Build the app
FROM gradle:8.2.1-jdk17 AS build
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
COPY gradlew .
COPY gradle/wrapper gradle/wrapper
# Make gradlew executable
RUN chmod +x gradlew

# Build the application (skip tests for faster build)
RUN ./gradlew build -x test --no-daemon

# Stage 2: Run the app
FROM openjdk:17-jdk-alpine
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]