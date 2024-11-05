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
        stage(' Nexus') {
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
                    sh 'mvn clean deploy -DskipTests -s /var/lib/jenkins/.m2/settings.xml'
                }
            }
        }
        stage('Docker Image') {
            steps {
                // Étape de packaging Maven
                sh 'mvn clean package -DskipTests'

                // Étape de construction de l'image Docker
                sh 'docker build -t aliyounes/gestion-station-ski:1.4 .'
            }
        }
        stage('Docker Hub') {
            steps {
                // Connexion à Docker Hub
                sh 'docker login -u aliyounes -p201JMT4012'

                // Pousser l'image sur Docker Hub
                sh 'docker push aliyounes/gestion-station-ski:1.4'
            }
        }
        stage('Docker Compose') {
            steps {
                script {
                    // Lancer Docker Compose pour démarrer les conteneurs
                    sh 'docker compose up -d'

                    // Pause pour s’assurer que les conteneurs ont le temps de démarrer
                    sh 'sleep 10'

                    // Vérification du conteneur app-spring
                    def appContainer = sh(script: "docker ps | grep app-spring", returnStatus: true)
                    if (appContainer == 0) {
                        echo "Création du conteneur Spring réussie pour gestion-station-ski."
                    } else {
                        error "Échec du démarrage du conteneur Spring pour gestion-station-ski."
                    }

                    // Vérification du conteneur mysqldb
                    def dbContainer = sh(script: "docker ps | grep mysqldb", returnStatus: true)
                    if (dbContainer == 0) {
                        echo "Création du conteneur MySQL réussie pour MySQL 5.7."
                    } else {
                        error "Échec du démarrage du conteneur MySQL pour MySQL 5.7."
                    }
                }
            }

        }
         stage('Grafana') {
                        steps {
                            script {
                                // Démarre le conteneur mysql-exporter2
                                echo "Démarrage du conteneur mysql-exporter2..."
                                sh 'docker start mysql-exporter2'

                                // Pause de 180 secondes
                                sleep 180

                                // Vérifie si mysql-exporter2 est en cours d’exécution
                                def mysqlExporterContainer = sh(script: "docker ps | grep mysql-exporter2", returnStatus: true)
                                if (mysqlExporterContainer == 0) {
                                    echo "Création du conteneur mysql-exporter2 réussie."
                                } else {
                                    error "Échec du démarrage du conteneur mysql-exporter2."
                                }

                                // Démarre le conteneur prometheus
                                echo "Démarrage du conteneur prometheus..."
                                sh 'docker start prometheus'

                                // Pause de 180 secondes
                                sleep 180

                                // Vérifie si prometheus est en cours d’exécution
                                def prometheusContainer = sh(script: "docker ps | grep prometheus", returnStatus: true)
                                if (prometheusContainer == 0) {
                                    echo "Création du conteneur prometheus réussie."
                                } else {
                                    error "Échec du démarrage du conteneur prometheus."
                                }

                                // Démarre le conteneur grafana
                                echo "Démarrage du conteneur grafana..."
                                sh 'docker start grafana'

                                // Pause de 180 secondes
                                sleep 180

                                // Vérifie si grafana est en cours d’exécution
                                def grafanaContainer = sh(script: "docker ps | grep grafana", returnStatus: true)
                                if (grafanaContainer == 0) {
                                    echo "Création du conteneur grafana réussie."
                                } else {
                                    error "Échec du démarrage du conteneur grafana."
                                }
                            }
         }               }
         stage('K8S') {
                     steps {
                         script {
                             // Démarrer le serveur K3s
                             sh 'docker start k3s-server'
                             echo "Serveur K3s démarré"

                             // Appliquer le fichier de déploiement Kubernetes
                             sh 'kubectl apply -f kubernetes-deployment.yml'

                             // Attendre que les pods soient prêts
                             echo "Attente de la disponibilité des pods..."
                             sh '''
                             until kubectl get pods -l app=app-spring -o jsonpath='{.items[0].status.containerStatuses[0].ready}' | grep -q "true"; do
                                 echo "Pod app-spring pas encore prêt, attente de 5 secondes..."
                                 sleep 5
                             done
                             '''

                             echo "Le pod app-spring est prêt !"

                             // Récupérer l'adresse IP du node
                             def nodeIP = sh(script: "kubectl get nodes -o jsonpath='{.items[0].status.addresses[0].address}'", returnStdout: true).trim()

                             // Afficher l'URL d'accès pour vérification
                             echo "Application accessible à http://${nodeIP}:30001/api/skier/getSkiersBySubscription"
                         }
                     }
         }







    }
}
