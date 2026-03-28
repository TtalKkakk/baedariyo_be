pipeline {
    agent any

    environment {
        IMAGE_NAME = "ttalkkak"
        CONTAINER_NAME = "ttalkkak-container"
        GIT_URL = "https://github.com/TtalKkakk/baedariyo_be.git"
    }

    stages {

        stage('Checkout') {
            steps {
                git url: "${GIT_URL}", branch: 'main'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker stop $CONTAINER_NAME || true
                docker rm $CONTAINER_NAME || true
                docker run -d -p 8081:8080 --name $CONTAINER_NAME $IMAGE_NAME
                '''
            }
        }
    }
}