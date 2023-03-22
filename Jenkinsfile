pipeline {
    agent any

     environment {
            CI = 'true'
        }
    stages {
        stage('Build') {
            steps {
                sh 'npm install'
            }
        }
        stage('Test') {
                    steps {
	                echo 'Hello World !! This is second step'

                    }
                }
                stage('Deliver') {
                            steps {
		                echo 'Hello World !! This is third step'
                            }
                        }

    }
}
