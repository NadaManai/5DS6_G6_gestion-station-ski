pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Checkout') {
            steps {
                //checkout scmGit(branches: [[name: '*/Nour']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-trigger-token', url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git']])
                slackSend message: 'I cloned my branch '
                git branch: 'Nour', credentialsId: 'devops-pipeline', url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }

        stage('Clean and Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }
/*
    stage('Test') {
             steps {
                 sh 'mvn test -Dtest=SubscriptionServicesImplTest'
             }
             post {
                 always {

                 }
                 failure {
                     slackSend message: 'Tests failed. Please check the report for details.'
                 }
                 success {
                     slackSend message: 'Tests ran successfully!'
                 }
             }
         }
*/
        stage('Sonarqube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar-server') {

                    sh '''
                        mvn test jacoco:report
                        chmod +x mvnw
                        ./mvnw clean package
                        ./mvnw clean compile org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.url=http://192.168.1.20:9000/ -Dsonar.login=squ_22403973d23165d1f6c677a22be4ccf457a87f4a -Dsonar.projectName=5DS6_G6_gestion-station-ski -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }

                  post {
                           always {
                                    jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/target/test-classes'
                                }
                            }
            }
            }

       stage('Quality gate') {
                           steps {
                               timeout(time:2, unit: 'MINUTES'){
                               waitForQualityGate abortPipeline: true
                               }
                           }
                       }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }


        stage('OWASP Dependency Check') {
             steps {
                        slackSend message: 'Checking dependencies'
                        dependencyCheck additionalArguments: '--scan target/', odcInstallation: 'DP-Check'
                        dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
                   }
                }

        stage('Maven Deploy to Nexus') {
            steps {
                configFileProvider([configFile(fileId: '571f55fb-0ae2-4456-b089-9458c4925c67', targetLocation: 'mavensettings')]) {
                    slackSend message: 'I deployed my app to nexus'
                    sh "mvn -s mavensettings clean deploy -DskipTests=true"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withDockerRegistry(credentialsId: '27f21e11-c55f-4dc7-8c6b-d586ce645eb0', toolName: 'docker') {
                        sh 'docker build -t station-ski-nour -f docker/Dockerfile .'
                        sh 'docker tag station-ski-nour kchaounour/station-ski-nour:latest'
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withDockerRegistry(credentialsId: '27f21e11-c55f-4dc7-8c6b-d586ce645eb0', toolName: 'docker') {
                        sh 'docker push kchaounour/station-ski-nour:latest'
                        slackSend message: 'I pushed my img into DockerHub'
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

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    sh '''
                        docker compose down || true
                        docker compose up -d --build --no-color --wait
                        docker compose ps
                    '''
                    slackSend message: 'Starting app container, our app is working yay !!'
                }
            }
        }

        stage('Prometheus Grafana') {
            steps {
                script {
                    sh '''
                        docker start grafana
                        docker start prometheus
                        docker ps -a
                    '''
                    slackSend message: "Let's visualize the dashboard at: http://192.168.1.20:3000/d/haryan-jenkins/jenkins3a-performance-and-health-overview?from=now-30m&to=now&timezone=browser"
                }
            }
        }

    }
}
