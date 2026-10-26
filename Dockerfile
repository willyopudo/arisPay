# syntax=docker/dockerfile:1.7

# Use Eclipse Temurin JDK 21 as the base image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom files first (for better layer caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY domain/pom.xml domain/pom.xml
COPY infrastructure/pom.xml infrastructure/pom.xml
COPY application-api/pom.xml application-api/pom.xml

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN chmod +x ./mvnw
RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache \
    ./mvnw dependency:go-offline -B

# Copy source code
COPY domain/src domain/src
COPY infrastructure/src infrastructure/src
COPY application-api/src application-api/src

# Build the application
RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache \
    ./mvnw clean package -DskipTests -B

# Create a new stage for the runtime image (multi-stage build for smaller image)
FROM eclipse-temurin:21-jre-jammy

# Install curl for health checks (optional)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=0 /app/application-api/target/application-api-1.0-SNAPSHOT.jar ArisPay.jar

# Prepare writable app paths and ownership for non-root runtime
RUN mkdir -p /app/media && chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8082

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ArisPay.jar"]