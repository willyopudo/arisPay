# syntax=docker/dockerfile:1.7

# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
# Normalize CRLF and set the exec bit (survives Windows checkouts)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache,sharing=locked \
    ./mvnw dependency:go-offline -q

COPY src src
RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache,sharing=locked \
    ./mvnw clean package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy

RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app
COPY --from=build --chown=appuser:appuser /app/target/*.jar app.jar
USER appuser

EXPOSE 8888

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]