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
                // Exécute 'mvn clean install' pour installer et compiler
                sh 'mvn clean install -U -DskipTests'
                // Ensuite, 'mvn package' pour générer le package final
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Mockito Tests') {
            steps {
                script {
                    echo "Démarrage des tests unitaires avec Mockito..."
                    sh 'mvn test -Dspring.profiles.active=test'
                    echo "Tests unitaires terminés avec succès !"
                }
            }
            post {
                success {
                    echo "Tous les tests unitaires sont réussis."
                }
                failure {
                    echo "Des tests unitaires ont échoué."
                }
            }
        }
    }
}
