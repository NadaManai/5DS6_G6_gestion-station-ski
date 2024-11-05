pipeline {
    agent any

    stages {
        stage('GIT') {
            steps {
                echo 'Recup Code de GIT :'
                git branch: 'eyabenahmed',
                    url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }
        stage('Maven Clean') {
            steps {
                echo 'Nettoyage du Projet :'
                sh 'mvn clean compile'
            }
        }
        stage('Maven Build') {
            steps {
                echo 'Building the project:'
                sh 'mvn clean package'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                echo 'Analyse de la Qualité du code :'
                sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=Eya12345678@'
            }
        }
        stage('JUNIT/MOCKITO') {
            steps {
                echo 'JUnit Tests :'
                sh 'mvn test'
            }
        }
        stage('Build image') {
            steps {
                echo 'Building image :'
                sh 'docker build -t eya32/gestion-station-ski:1.0.0 .'
            }
        }
        stage('Pushing image') {
            steps {
                echo 'Pushing image :'
                sh '''
                docker login -u eya32 -p Eya11157882.
                docker push eya32/gestion-station-ski:1.0.0
                '''
            }
        }
        stage('Docker Compose Up') {
            steps {
                echo 'Starting services with Docker Compose :'
                sh 'docker compose up -d'
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
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}