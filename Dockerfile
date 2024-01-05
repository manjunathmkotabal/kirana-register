# Use the specified JDK base image
FROM eclipse-temurin:18-jdk-focal

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper configuration
COPY .mvn/ ./.mvn

# Copy the Maven wrapper script and the Project Object Model (POM) file
COPY mvnw pom.xml ./

# Copy the entire source code into the container
COPY src ./src

# Build the Spring Boot application and create the JAR inside the container
RUN ./mvnw dependency:go-offline

# Expose the port your app is running on (if necessary)
EXPOSE 8080

# Command to run your application using the Maven wrapper and the generated JAR
CMD ["./mvnw", "spring-boot:run"]
