# Use an official OpenJDK runtime as a parent image
FROM maven:3.9.6-eclipse-temurin-21-alpine

# Create new app directory
RUN mkdir /app

# Set the working directory in the container
WORKDIR /app

# Copy the project files
COPY /src ./src
COPY pom.xml .

# Compile the Java source code
RUN mvn clean compile assembly:single

# Specify the command to run on container start
CMD ["java", "-jar", "./target/HentaiBot-1.1-jar-with-dependencies.jar"]