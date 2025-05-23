# Build stage
FROM eclipse-temurin:21-jdk as build

WORKDIR /app

# Copy Gradle wrapper and build files for dependency caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Download dependencies (will be cached if unchanged)
RUN ./gradlew build -x test --no-daemon || true

# Copy ALL source files and build JAR
COPY . .

RUN ./gradlew bootJar -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]