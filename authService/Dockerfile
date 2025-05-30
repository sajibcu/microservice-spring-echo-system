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


# Download and add APM agent
RUN wget -O elastic-apm-agent.jar https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.53.0/elastic-apm-agent-1.53.0.jar


# Stage 2: Run the app
FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/elastic-apm-agent.jar elastic-apm-agent.jar

ENTRYPOINT ["java", "-javaagent:elastic-apm-agent.jar", "-jar", "app.jar"]