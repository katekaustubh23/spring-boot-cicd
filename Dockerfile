# Use a base image with Java
FROM eclipse-temurin:17-jre-jammy AS build
# Make the source directory in the image
WORKDIR /app
# Copy the Maven project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# Copy the source code
COPY src src

# Build the application
# Use -DskipTests to speed up the image build, tests will run in Jenkins
RUN ./mvnw clean package -DskipTests

# Final stage/Runtime image
FROM eclipse-temurin:17-jre-jammy
# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 8088
# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar
# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]