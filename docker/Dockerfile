FROM openjdk:8u151-jdk-alpine3.7

WORKDIR /APP_HOME

COPY target/gestion-station-ski-1.0.jar app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]
