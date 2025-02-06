# Use latest OpenJDK
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy everything into the container
COPY . /app

# Grant execute permission to the Maven wrapper
RUN chmod +x mvnw

# Resolve dependencies and build the application
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the built JAR file
CMD ["java", "-jar", "target/url-shortener-backend.jar"]
