FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/gestion-station-ski-1.0.jar /app/gestion-station-ski-1.0.jar

EXPOSE 8098

ENTRYPOINT ["java","-jar","/app/gestion-station-ski-1.0.jar"]
