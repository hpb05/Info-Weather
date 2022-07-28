pipeline {
    agent any
    environment {
        HOME = "${WORKSPACE}"
        NPM_CONFIG_CACHE = "${WORKSPACE}/.npm"
        WEB_APPID = 'e7f317c14cc61b4f920f60fcdcc10d06'
        DOCKER_CREDS=credentials('DOCKER_CREDS')
    }

    stages {
        stage('Docker Image Pull and Deploy') {
            steps {
                sh "pwd"
                sh "ls -la"
                sh 'echo $DOCKER_CREDS_PSW | docker login -u $DOCKER_CREDS_USR --password-stdin'
                sh 'docker pull haribekal/weather-info-app:latest'
                sh 'docker run --env WEB_APPID=$WEB_APPID -p 9090:9090 --name weather-info-app -d haribekal/weather-info-app:latest'
            }
        }
    }
}