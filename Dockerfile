# STAGE 1: Build the application
# Use a Java 17 JDK image as the builder
FROM eclipse-temurin:17-jdk-jammy AS builder

# Set the working directory
WORKDIR /workspace

# 1. Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 2. Download dependencies
# This creates a separate layer that is cached.
# It will only re-run if the build.gradle.kts file changes.
RUN ./gradlew dependencies

# 3. Copy the source code
# This is the critical step that was missing.
# If your code changes, the build will restart from this layer.
COPY src ./src

# 4. Build the executable jar (now with source code)
RUN ./gradlew bootJar

# STAGE 2: Create the final, lightweight image
# Use a JRE (Java Runtime Environment) image which is smaller
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# 5. Copy the built jar from the 'builder' stage
# This jar will now be complete and contain all your classes.
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Expose the port that the Spring application runs on
EXPOSE 8080

# This is the command that will run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]