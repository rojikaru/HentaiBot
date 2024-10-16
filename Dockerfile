# Use an maven image as a base image
FROM maven:3.9.9-eclipse-temurin-23-alpine

# Set the working directory in the container
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:resolve

COPY . .

# Install dependencies
RUN mvn clean install

# Compile the code
RUN mvn clean compile assembly:single

# Specify the command to run on container start
CMD ["java", "-jar", "./target/HentaiBot-1.1-jar-with-dependencies.jar"]
