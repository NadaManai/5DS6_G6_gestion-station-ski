pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Checkout') {
            steps {
                echo "Cloning branch Nour"
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
                echo "Testing"
                sh 'mvn test'
            }
        }

        stage('Sonarqube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar-server') {
                    sh '''
                        chmod +x mvnw
                        ./mvnw clean package
                        ./mvnw clean compile org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.url=http://192.168.1.20:9000/ -Dsonar.login=squ_22403973d23165d1f6c677a22be4ccf457a87f4a -Dsonar.projectName=5DS6_G6_gestion-station-ski -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml -Pcoverage
                    '''
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }


        // dependecy check here

        stage('Maven Deploy to Nexus') {
            steps {
                configFileProvider([configFile(fileId: '571f55fb-0ae2-4456-b089-9458c4925c67', targetLocation: 'mavensettings')]) {
                    // Directly use the path in the sh command
                    sh "mvn -s mavensettings clean deploy -DskipTests=true"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                    script{
                                withDockerRegistry(credentialsId: '27f21e11-c55f-4dc7-8c6b-d586ce645eb0', toolName: 'docker')  {
                                    sh 'sudo docker build -t station-ski-nour -f docker/Dockerfile .'
                                    sh 'sudo docker tag station-ski-nour kchaounour/station-ski-nour:latest' //naming docker img besh baed npushiwha
                    }

            }

            }
        }
    }
}
