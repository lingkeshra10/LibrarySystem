# Use an official OpenJDK runtime as a base image
FROM eclipse-temurin:21-jre-alpine

# Make the Lib Service Directory
RUN mkdir /opt/LibService

# Set the working directory
WORKDIR /opt/LibService

# Create an application properties file
RUN touch /opt/LibService/application.properties

# Set the environment value
ENV APPLICATION_PROPERTIES_FILE_PATH="/opt/LibService/application.properties"

# Copy the JAR file into the container
COPY /target/librarysystem-0.0.1-SNAPSHOT.jar app.jar

# Command to run the Spring Boot application
CMD ["java", "-jar", "/opt/LibService/app.jar"]