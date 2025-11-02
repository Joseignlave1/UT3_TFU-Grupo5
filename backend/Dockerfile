# Use Java 21 JDK base image
FROM eclipse-temurin:21-jdk

# Add JAR file built by Maven/Gradle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose port 8080 (same as Spring Boot default)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java","-jar","/app.jar"]
