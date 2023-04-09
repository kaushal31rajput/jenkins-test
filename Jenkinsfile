@Library('first-shared-lib') _
import org.jenkinsci.plugins.docker.workflow.*


pipeline {
  agent any
  stages {
    stage('Check file 1') {
      steps {
        sh 'cat file-1.txt'
      }
    }
    stage('Check file 2') {
      steps {
        sh 'cat file-2.txt'
      }
    }

stage('Install dependencies') {
  steps {
    script {
      if (isCacheValid("gs://my-new-bucket-12344321-kaushal")) {
         cacheDownload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "npm-ci-cache"])
      } else {
        sh "npm ci" 
      }
    }
  }
}

    stage('Run test cases') {
      steps {
        sh '''# define where you want the test results
export JUNIT_REPORT_PATH=./test-results.xml

## run mocha and tell it to use the JUnit reporter
npx mocha --reporter mocha-jenkins-reporter'''
        sh 'ls -lart'
	sh 'pwd'
      }
    }
  }
  post {
    always {
        junit 'test-results.xml'
  	cacheUpload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "npm-ci-cache"])

    }
  }
}
