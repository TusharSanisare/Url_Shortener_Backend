# Use the latest stable OpenJDK version
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and source files
COPY . /app

# Resolve dependencies and build the application
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the built JAR file
CMD ["java", "-jar", "target/url-shortener-backend.jar"]
