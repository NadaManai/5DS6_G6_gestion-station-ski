pipeline {
    agent any
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

    stage('Sonarqube Analysis') {
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }
        steps {
        withSonarQubeEnv(installationName: 'sonar-server') {
           sh '''
                chmod +x mvnw
                ./mvnw clean package
               ./mvnw clean  org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar
           '''
       }
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

