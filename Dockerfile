# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim

# Author label
LABEL authors="josdwep"

# Set working directory inside container
WORKDIR /app

# Copy your jar file into the container
COPY build/libs/ems-0.0.1-SNAPSHOT.jar app.jar
# or for Maven: COPY target/ems-0.0.1-SNAPSHOT.jar app.jar

# Expose port (replace with your actual app port)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
