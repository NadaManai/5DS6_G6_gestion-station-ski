FROM openjdk:8u151-jdk-alpine3.7

EXPOSE 8089

COPY target/gestion-station-ski-1.0.jar app.jar

WORKDIR APP_HOME

ENTRYPOINT exec java -jar app.jar