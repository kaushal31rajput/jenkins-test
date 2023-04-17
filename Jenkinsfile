@Library('first-shared-lib') _
import org.jenkinsci.plugins.docker.workflow.*
import com.hcl.icontrol.jenkins.ChecksumUtils 



pipeline {
  agent any

  environment {
    PROJECT_NAME = "icontrol-web"
    NPM_CI_CACHE = "${env.JOB_NAME}-NPM-CACHE"
    file1 = "package.json"
    file2 = "package-lock.json"
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
          sh "ls -lart"
          //String checksum = ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")
	  //println "Content of checksum: ${checksum}"
	  //sh "echo value for checksum: ${checksum}"
	  if (isCacheValid([file1: "package.json", file2: "package-lock.json", NAME: "NPM-CACHE"])) {
	      cacheDownload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "NPM_CI_CACHE"]) 
	  sh "echo cache found"
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
  	cacheUpload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "NPM_CI_CACHE"])

    }
  }
}
