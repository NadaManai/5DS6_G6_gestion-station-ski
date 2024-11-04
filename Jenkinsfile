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

    triggers {
        // Test webhook
        githubPush()
    }

    stages {
        stage('GIT') {
            steps {
                33git credentialsId: 'DevOps_Project', branch: 'NadaManai_5DS6_G6', url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
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
                sh 'mvn test jacoco:report'
                sh "mvn sonar:sonar -Dsonar.projectKey=DevOps-Project -Dsonar.host.url=http://192.168.0.33:9000 -Dsonar.login=sqa_68cd8eba8f84e6b8410680b8dec543f19320743f -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
            }
            post {
                always {
                    jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/target/test-classes'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'mvn package -DskipTests'
                sh "docker build -t ${DOCKER_IMAGE_NAME}:latest ."
            }
        }

        stage('Publish Docker Image') {
            steps {
                script {
                    sh '''
                        echo "${DOCKER_REGISTRY_CREDENTIALS_PSW}" | docker login --username "${DOCKER_REGISTRY_CREDENTIALS_USR}" --password-stdin
                    '''
                    sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy-container') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose up -d'
                sh 'sleep 30'

                // Verify deployment
                sh 'docker-compose ps'
            }
        }

        stage('Postman Test') {
            steps {
                script {
                    sleep(10)
                    sh '''
                        curl -X PUT http://192.168.0.110:8089/api/instructor/addAndAssignToCourse/2 \
                        -H "Content-Type: application/json" \
                        -d '{
                            "firstName": "Nada",
                            "lastName": "Manai",
                            "dateOfHire": "2023-10-01"
                        }'
                    '''
                    sh 'curl -X GET http://192.168.0.110:8089/api/instructor/all'
                }
            }
        }

        stage('Grafana and Prometheus') {
            steps {
                script {
                    sh 'docker start prometheus || docker run -d --name prometheus -p 9090:9090 -v /path/to/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus'
                    sh 'docker start grafana || docker run -d --name grafana -p 3000:3000 grafana/grafana'
                }
            }
        }
    }

    post {
        success {
            script {
                emailext (
                    subject: "✅ Build Successful - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <h2>✅ Build Successful</h2>
                        <p>Good news! The build for <strong>${env.JOB_NAME} #${env.BUILD_NUMBER}</strong> was successful. 🎉</p>
                        <h3>Build Summary:</h3>
                        <ul>
                            <li><strong>Project:</strong> ${env.JOB_NAME}</li>
                            <li><strong>Build Number:</strong> ${env.BUILD_NUMBER}</li>
                            <li><strong>Status:</strong> Success</li>
                            <li><strong>Timestamp:</strong> ${new Date()}</li>
                        </ul>
                        <h3>Postman Test Results:</h3>
                        <p>Here are the results of the GET request for all instructors.</p>
                        <pre>${sh(script: 'curl -s http://192.168.0.110:8089/api/instructor/all', returnStdout: true)}</pre>
                        <p>Keep up the good work! 🚀</p>
                    """,
                    mimeType: 'text/html',
                    to: 'nada.manai@esprit.tn'
                )
            }
        }
        failure {
            script {
                def logLines = currentBuild.rawBuild.getLog(10).join('\n') 
                emailext (
                    subject: "❌ Build Failed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <h2>❌ Build Failed</h2>
                        <p>Unfortunately, the build for <strong>${env.JOB_NAME} #${env.BUILD_NUMBER}</strong> has failed. 😞</p>
                        <h3>Build Summary:</h3>
                        <ul>
                            <li><strong>Project:</strong> ${env.JOB_NAME}</li>
                            <li><strong>Build Number:</strong> ${env.BUILD_NUMBER}</li>
                            <li><strong>Status:</strong> Failed</li>
                            <li><strong>Timestamp:</strong> ${new Date()}</li>
                        </ul>
                        <h3>Error Log:</h3>
                        <pre>${logLines}</pre>
                        <p>Please review the logs to identify and address the issues. 🔍</p>
                    """,
                    mimeType: 'text/html',
                    to: 'nada.manai@esprit.tn'
                )
            }
        }
    }
}
