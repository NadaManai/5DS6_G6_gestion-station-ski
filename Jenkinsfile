pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    environment {
        DOCKER_IMAGE_NAME = 'nadamanai/nadamanai-5ds6-g6-gestion-station-ski'
        DOCKER_REGISTRY_CREDENTIALS = credentials('dockerhub_credentials')
        DOCKER_REGISTRY_URL = 'https://registry.hub.docker.com'   
    }

    stages {
        stage('GIT') {
            steps {
                git credentialsId: 'DevOps_Project', branch: 'NadaManai_5DS6_G6', url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test Stage') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    sh "mvn sonar:sonar -Dsonar.projectKey=DevOps-Project -Dsonar.host.url=http://192.168.0.33:9000 -Dsonar.login=sqa_68cd8eba8f84e6b8410680b8dec543f19320743f"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    sh 'mvn deploy'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'mvn package -DskipTests'  
                    sh 'docker build -t ${DOCKER_IMAGE_NAME}:latest .'
                }
            }
        }

        stage('Publish Docker Image') {
            steps {
                script {
                    // Connexion à Docker Hub avec le token d'accès
                    sh '''
                        echo "${DOCKER_REGISTRY_CREDENTIALS_PSW}" | docker login --username "${DOCKER_REGISTRY_CREDENTIALS_USR}" --password-stdin
                    '''
                    sh 'docker push ${DOCKER_IMAGE_NAME}:latest'
                }
            }
        }

        stage('Run with Docker Compose') {
            steps {
                script {
                    sh 'docker-compose down'  
                    sh 'docker-compose up -d' 
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline finished successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
