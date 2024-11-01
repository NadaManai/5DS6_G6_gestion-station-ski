pipeline {
    agent any
    options{
    buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Checkout') {
            steps {

              echo "clone branch Nour"
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
              echo "testing"
              sh 'mvn test'
                   }
                      }

    stage('Sonarqube Analysis') {
        steps {
        withSonarQubeEnv(installationName: 'sonar-server') {
           sh '''
                chmod +x mvnw
                ./mvnw clean package
               ./mvnw clean compile org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.url=http://192.168.1.20:9000/ -Dsonar.login=squ_22403973d23165d1f6c677a22be4ccf457a87f4a  -Dsonar.projectName=5DS6_G6_gestion-station-ski  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml -Pcoverage
           '''
       }
    }
}
    stage('Build') {
                steps {
                   echo "deploying"
                    sh ' mvn build '
                }
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

