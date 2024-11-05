FROM openjdk:17

WORKDIR /app

COPY ./target/gestion-station-ski-1.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8089

CMD ["java", "-jar", "/app/app.jar"]
