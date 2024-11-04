pipeline {
    agent any

    stages {
        stage('Git') {
            steps {
                // Étape pour vérifier la connexion et cloner la branche
                git credentialsId: 'younesali0', branch: 'YounesAli-5DS6-G6',
                    url: 'https://github.com/NadaManai/5DS6_G6_gestion-station-ski.git'
            }
        }
    }
}
