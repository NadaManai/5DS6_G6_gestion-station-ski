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
/*
        stage('Sonarqube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar-server') {
                    sh '''
                        chmod +x mvnw
                        ./mvnw clean package
                        ./mvnw clean compile org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.url=http://192.168.1.20:9000/ -Dsonar.login=squ_22403973d23165d1f6c677a22be4ccf457a87f4a -Dsonar.projectName=5DS6_G6_gestion-station-ski -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml -Pcoverage
                    '''
                }
            }
        }
*/
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }


        // dependecy check here

        stage('Maven Deploy to Nexus') {
            steps {
                configFileProvider([configFile(fileId: '571f55fb-0ae2-4456-b089-9458c4925c67', targetLocation: 'mavensettings')]) {
                    sh "docker start nexus || true"
                    sh "mvn -s mavensettings clean deploy -DskipTests=true"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                    script{
                        withDockerRegistry(credentialsId: '27f21e11-c55f-4dc7-8c6b-d586ce645eb0', toolName: 'docker')  {
                        sh 'docker build -t station-ski-nour -f docker/Dockerfile .'
                        sh 'docker tag station-ski-nour kchaounour/station-ski-nour:latest' //naming docker img besh baed npushiwha
                    }
            }
            }
        }

        stage('Push Docker Image') {
                    steps {
                            script{
                                   withDockerRegistry(credentialsId: '27f21e11-c55f-4dc7-8c6b-d586ce645eb0', toolName: 'docker')  {
                                   sh 'docker push kchaounour/station-ski-nour:latest'
                            }
                    }

                    }
                }
/*
stage('Deploy To Docker Container') {
    steps {
        script {
            sh '''
                if [ "$(docker ps -aq -f name=station-ski-nour)" ]; then
                    docker rm -f station-ski-nour
                fi
                docker run -d --name station-ski-nour -p 8089:8089 kchaounour/station-ski-nour:latest
            '''
        }
    }
}

*/

        // TODO prune

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    sh '''
                            // Stop existing containers, without removing them
                               sh 'docker compose stop || true'
                               // Start containers without rebuilding them
                                sh 'docker compose start || true'
                                 // If you want to ensure they are built if changed, use this instead
                                  // sh 'docker compose up -d --no-build'
                    '''
                }
            }
        }

}

    }

