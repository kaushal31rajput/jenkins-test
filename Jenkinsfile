pipeline {
    agent {
        docker {
            image 'node:latest'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh 'npm ci --cache .npm --prefer-offline'
            }
        }

        stage('Test Async') {
            steps {
                sh 'node ./specs/start.js ./specs/async.spec.js'
            }
        }
    }

    post {
        always {
            sh 'npm cache clean --force'
        }
    }

    options {
        cache(caches: 'npm-cache', paths: ['.npm/'])
    }
}