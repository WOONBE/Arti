# Use the official OpenJDK 17 image from the Docker Hub as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
#WORKDIR /app

# Copy the JAR file from the host machine to the container
COPY build/libs/Arti-Backend-0.0.1-SNAPSHOT.jar Arti-Backend-0.0.1-SNAPSHOT.jar

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "Arti-Backend-0.0.1-SNAPSHOT.jar"]
