FROM openjdk:8u151-jdk-alpine3.7

# Set the working directory
WORKDIR /APP_HOME

# Copy the JAR file into the working directory
COPY target/gestion-station-ski-1.0.jar /APP_HOME/app.jar

# Expose the application's port
EXPOSE 8089

# Use exec form for ENTRYPOINT and correct path to the JAR file
ENTRYPOINT ["java", "-jar", "/APP_HOME/app.jar"]
