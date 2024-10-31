pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                // Clone the Git repository
                git credentialsId: 'DevOps_Project', branch: 'NadaManai_5DS6_G6', url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }

        stage('Compile Stage') {
            steps {
                // Compile the project
                sh 'mvn clean compile'
            }
        }

        stage('Test Stage') {
            steps {
                // Run unit tests
                sh 'mvn test'
            }
            post {
                always {
                    // Handle test results
                    junit '**/target/surefire-reports/*.xml' // Use this line if you generate JUnit test reports
                }
            }
        }
    }

    post {
        success {
            // Notifications or actions after success
            echo 'Pipeline finished successfully.'
        }
        failure {
            // Notifications or actions in case of failure
            echo 'Pipeline failed.'
        }
    }
}
