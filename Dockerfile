# =========================
# Build stage
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src
RUN mvn clean package -DskipTests

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Expose Spring Boot port
EXPOSE 8080

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
