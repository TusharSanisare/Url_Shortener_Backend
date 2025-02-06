FROM openjdk:23
WORKDIR /app
COPY . .
RUN ./mvnw dependency:resolve
EXPOSE 8081
CMD ["./mvnw", "spring-boot:run"]
