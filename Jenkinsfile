pipeline {
    agent any // Use any available agent
    
    stages {
        stage('Checkout') {
            steps {

              echo "clone repo"
                // Clone the repository
               // git 'https://github.com/your-repo.git'
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
    }

  
        stage('Release') {
            steps {
              echo "releasing"
               // 
            }
        }
    }
   

