
FROM eclipse-temurin:17-jdk-jammy AS builder


WORKDIR /workspace


COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .


RUN ./gradlew dependencies


COPY src ./src


RUN ./gradlew bootJar


FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app


COPY --from=builder /workspace/build/libs/*.jar app.jar

# Expose the port that the Spring application runs on
EXPOSE 8080

# This is the command that will run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]