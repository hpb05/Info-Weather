pipeline {
    agent any
    environment {
        HOME = "${WORKSPACE}"
        NPM_CONFIG_CACHE = "${WORKSPACE}/.npm"
        WEB_APPID = 'e7f317c14cc61b4f920f60fcdcc10d06'
        DOCKER_CREDS=credentials('DOCKER_CREDS')
    }

    stages {
        stage('Checkout from SCM') {
            steps {
                checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/hpb05/Info-Weather.git']]]
            }
        }

        stage('Build') {
            steps {
                sh "pwd"
                dir('info-weather') {
                    sh "pwd"
                    sh "ls -la"
                    sh "./mvnw clean package"
                }

            }
        }

        stage('Build Docker Image / Push') {
            steps {
                sh "pwd"
                sh "ls -la"
                sh "docker build -t weather-info-app ."
                sh "docker tag weather-info-app haribekal/weather-info-app:latest"
                sh 'echo $DOCKER_CREDS_PSW | docker login -u $DOCKER_CREDS_USR --password-stdin'
                sh 'docker push haribekal/weather-info-app:latest'
            }
        }
    }

    post {
        always {
            sh 'docker logout'
        }
    }
}