pipeline {
    agent any
    tools {
            jdk 'JAVA_HOME'       // Utilise le JDK configuré sous ce nom dans Jenkins
            maven 'M2_HOME'       // Utilise Maven configuré sous ce nom dans Jenkins
        }

    stages {
        stage('Git') {
            steps {
                // Étape pour vérifier la connexion et cloner la branche
                git credentialsId: 'younesali0', branch: 'YounesAli-5DS6-G6',
                    url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }
        stage('Maven Clean') {
                    steps {
                        // Exécute 'mvn clean' en évitant les tests
                        sh 'mvn clean -DskipTests'
                    }
                }
    }
}
