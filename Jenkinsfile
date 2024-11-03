pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Checkout') {
            steps {
                echo "Cloning branch Nour"
                // Clone the repository
                git branch: 'Nour', credentialsId: 'devops-pipeline', url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }

        stage('Clean and Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo "Testing"
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage('Maven Deploy to Nexus') {
            steps {
                configFileProvider([configFile(fileId: '571f55fb-0ae2-4456-b089-9458c4925c67', targetLocation: 'mavensettings')]) {
                    sh "mvn -s mavensettings clean deploy -DskipTests=true"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withDockerRegistry(credentialsId: '27f21e11-c55f-4dc7-8c6b-d586ce645eb0', toolName: 'docker')  {
                        sh 'docker build -t station-ski-nour -f docker/Dockerfile .'
                        sh 'docker tag station-ski-nour kchaounour/station-ski-nour:latest'
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withDockerRegistry(credentialsId: '27f21e11-c55f-4dc7-8c6b-d586ce645eb0', toolName: 'docker')  {
                        sh 'docker push kchaounour/station-ski-nour:latest'
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {

                    dir('docker-compose.yml') {
                        sh '''
                            docker compose down || true
                            docker compose up -d --build --no-color --wait
                            docker compose ps
                        '''
                    }
                }
            }
        }
    }
}
