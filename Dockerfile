# Utiliser l'image Java 17 officielle
FROM openjdk:17-jdk-slim

# Exposer le port de l'application Spring Boot
EXPOSE 8089

# Ajouter le fichier JAR généré par Maven dans l'image
ADD target/gestion-station-ski-1.1.jar app.jar

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]
