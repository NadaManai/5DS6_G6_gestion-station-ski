pipeline {
    agent any // Use any available agent
    
    stages {
        stage('Checkout') {
            steps {

              echo "clone branch Nour"
                // Clone the repository
                git branch: 'Nour',
                    credentialsId: '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',
                    url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski/tree/Nour'
            }
        }

        stage('Build') {
            steps {

               echo "building"
                // Example build step using Maven
               // sh 'mvn clean package'
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

