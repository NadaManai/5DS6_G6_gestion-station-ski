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

    stage('Build'){
            steps{
            sh ' mvn clean package -DskipTests=true'
            }
    }
}
    stage('Maven Deploy to nexus') {
                steps {
                    configFileProvider([configFile(fileId: '571f55fb-0ae2-4456-b089-9458c4925c67', targetLocation: 'mavensettings')]) {

                    sh 'mvn -s ${mavensettings} clean deploy -DskipTests=true'

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
}

