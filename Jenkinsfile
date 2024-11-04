pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('Git') {
            steps {
                git credentialsId: 'younesali0', branch: 'YounesAli-5DS6-G6',
                    url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }

        stage('Maven Clean') {
            steps {
                sh 'mvn clean install -U -DskipTests'
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

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Démarrer SonarQube en Docker
                    sh 'docker start sonarqube'

                    // Pause pour s'assurer que SonarQube démarre correctement
                    sh 'sleep 30'

                    // Exécuter l'analyse SonarQube
                    sh 'mvn sonar:sonar -Dspring.profiles.active=test -Dsonar.projectKey=gestion-station-ski -Dsonar.host.url=http://localhost:9000 -Dsonar.login=sqa_40c91d3bd3e61c5131e16c52b8dd1990c2e80156'
                }
            }
        }
    }
}
