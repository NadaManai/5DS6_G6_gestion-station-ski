pipeline {
    agent any // Use any available agent
    /*
    environment{
    SCANNER_HOME = tool 'sonarqube-scanner'
    }
    */
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
        JAVA_HOME = '/usr/lib/jvm/java-11-openjdk-amd64' // specify the directory only, not the java binary
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }
        steps {
       echo "Running SonarQube Analysis with SonarQube Maven Plugin 3.9.0.2155 and Java 11"
        withSonarQubeEnv(installationName: 'sonar-server', credentialsId: 'sonar-token') {
           sh '''
               mvn clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar \
                   -Dsonar.host.url=https://your-sonarqube-server
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

