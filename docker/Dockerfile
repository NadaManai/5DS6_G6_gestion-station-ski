FROM openjdk:8u151-jdk-alpine3.7

EXPOSE 8089

ENV APP_HOME /usr/src/app

COPY target/gestion-station-ski-1.0.jar $APP_HOME/app.jar

WORKDIR APP_HOME

ENTRYPOINT exec java -jar app.jar