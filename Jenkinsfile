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

                    // Boucle pour vérifier si SonarQube est prêt
                    sh '''
                    echo "Attente que SonarQube soit opérationnel..."
                    until curl -s http://localhost:9000/api/system/status | grep -q "UP"; do
                        echo "SonarQube n'est pas encore prêt, attente de 10 secondes..."
                        sleep 10
                    done
                    echo "SonarQube est maintenant prêt !"
                    '''

                    // Exécuter l’analyse SonarQube
                    sh 'mvn sonar:sonar -Dspring.profiles.active=test -Dsonar.projectKey=gestion-station-ski -Dsonar.host.url=http://localhost:9000 -Dsonar.login=sqa_40c91d3bd3e61c5131e16c52b8dd1990c2e80156'
                }


            }
        }
        stage('Start, Check, and Deploy to Nexus') {
            steps {
                script {
                    // Démarre le conteneur Nexus
                    sh 'docker start nexus'

                    // Vérifier que Nexus est bien démarré avec une vérification sur docker ps
                    def nexusRunning = sh(script: 'docker ps | grep nexus', returnStatus: true) == 0
                    if (!nexusRunning) {
                        error "Le conteneur Nexus n'est pas en cours d'exécution. Veuillez vérifier la configuration."
                    } else {
                        echo "Nexus est démarré et opérationnel."
                    }

                    // Pause pour s'assurer que Nexus est bien initialisé
                    sh 'sleep 10'

                    // Déployer sur Nexus sans injection de credentials
                    sh 'mvn deploy -DskipTests -s /var/lib/jenkins/.m2/settings.xml -X'
                }
            }
        }




    }
}
