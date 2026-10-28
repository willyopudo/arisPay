# syntax=docker/dockerfile:1.7

# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper and pom files first (for better layer caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY domain/pom.xml domain/pom.xml
COPY infrastructure/pom.xml infrastructure/pom.xml
COPY application-api/pom.xml application-api/pom.xml

# Strip Windows CRLF line endings and set the exec bit on the Maven wrapper
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Download dependencies (cached unless a pom.xml changes)
RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache,sharing=locked \
    ./mvnw dependency:go-offline -B

# Copy source code
COPY domain/src domain/src
COPY infrastructure/src infrastructure/src
COPY application-api/src application-api/src

# Build the application
RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache,sharing=locked \
    ./mvnw clean package -DskipTests -B

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy

# curl for health checks; non-root app user
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

# Writable media directory for uploads
RUN mkdir -p /app/media && chown appuser:appuser /app/media

# Copy the built JAR from the build stage, owned by the app user
COPY --from=build --chown=appuser:appuser \
     /app/application-api/target/application-api-1.0-SNAPSHOT.jar ArisPay.jar

USER appuser

EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# exec so Java runs as PID 1 and receives SIGTERM for graceful shutdown
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar ArisPay.jar"]