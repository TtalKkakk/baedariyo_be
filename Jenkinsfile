pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                git url: 'https://github.com/your-repo.git', branch: 'main'
                echo 'Jenkins 동작 확인'
            }
        }
    }
}