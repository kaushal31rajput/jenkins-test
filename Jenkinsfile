@Library('first-shared-lib') _
import org.jenkinsci.plugins.docker.workflow.*
import com.hcl.icontrol.jenkins.ChecksumUtils 



pipeline {
  agent any

  environment {
    PROJECT_NAME = "icontrol-web"
    NPM_CI_CACHE = "${env.JOB_NAME}-npm-ci-cache"
  }
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
        cache(maxCacheSize: 250, caches: [
          arbitraryFileCache(path: 'node_modules', cacheValidityDecidingFile: 'package-lock.json')
])
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

    }
  }
}
