# STAGE 1: Build the application
# Use a Java 17 JDK image as the builder
FROM eclipse-temurin:17-jdk-jammy AS builder

# Set the working directory
WORKDIR /workspace

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle ./gradle

# Copy the build and settings files
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Run Gradle build to download dependencies and build the jar
# We use bootJar to create the executable jar
RUN ./gradlew bootJar

# STAGE 2: Create the final, lightweight image
# Use a JRE (Java Runtime Environment) image which is smaller
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the built jar from the 'builder' stage
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Expose the port that the Spring application runs on
EXPOSE 8080

# This is the command that will run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]