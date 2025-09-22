# ---------- Stage 1: Build ----------
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy Gradle files first (for better caching)
COPY build.gradle settings.gradle gradlew* ./
COPY gradle ./gradle

# Download dependencies (cached)
RUN ./gradlew dependencies --no-daemon || return 0

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew build --no-daemon

# ---------- Stage 2: Run ----------
FROM azul/zulu-openjdk:17-latest
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
