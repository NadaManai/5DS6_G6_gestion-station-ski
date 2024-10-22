pipeline {
    agent any // Use any available agent
    tools{
            jdk 'JAVA_HOME'
            maven 'M2_HOME'
    }

    stages {
        stage('Checkout') {
            steps {

              echo "clone branch Nour"
                // Clone the repository
                git branch: 'Nour', credentialsId: 'devops-pipeline', url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }

        stage('Compile') {
            steps {

               echo "Compiling"
               sh "mvn clean compile"
            }
        }

        stage('Sonarqube Analysis') {
            steps {

               echo "Sonarqube"
            }
        }

        stage('Test') {
            steps {
               echo "testing"
                // Run unit tests
              //  sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
               echo "deploying"
                // Example deploy step (adjust according to your deploy strategy)
              //  sh 'scp target/your-app.jar user@staging-server:/path/to/deploy'
            }
        }
    
        stage('Release') {
            steps {
              echo "releasing"
               // 
            }
        }
    }
}

