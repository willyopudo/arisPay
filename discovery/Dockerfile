# syntax=docker/dockerfile:1.7

# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
# Strip Windows CRLF line endings and set the exec bit on the Maven wrapper
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache,sharing=locked \
  ./mvnw dependency:go-offline -q

COPY src src
RUN --mount=type=cache,target=/root/.m2,id=arispay-maven-cache,sharing=locked \
  ./mvnw clean package -DskipTests -q

# ── Stage 2: Runtime ───────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

COPY --from=build --chown=appuser:appuser /app/target/discovery-0.0.1-SNAPSHOT.jar app.jar
USER appuser

EXPOSE 8761

HEALTHCHECK --interval=15s --timeout=5s --start-period=45s --retries=5 \
  CMD curl -f http://localhost:8761/actuator/health || exit 1

ENV JAVA_OPTS="-Xmx256m -Xms128m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]