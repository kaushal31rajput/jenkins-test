#!/usr/bin/env groovy

import com.hcl.icontrol.jenkins.ChecksumUtils

def call(Map config) {
    try {
        String cacheKey = "JOB_NAME"
         log('DEBUG', "value for job_name  ${cacheKey}")
        echo "BUILD_NUMBER ::" "JOB_NAME"
        echo "BUILD_ID ::" $BUILD_ID
        echo "BUILD_DISPLAY_NAME ::" $BUILD_DISPLAY_NAME
        echo "JOB_NAME ::" $JOB_NAME
        echo "JOB_BASE_NAME ::" $JOB_BASE_NAME
        echo "BUILD_TAG ::" $BUILD_TAG
        echo "EXECUTOR_NUMBER ::" $EXECUTOR_NUMBER
        echo "NODE_NAME ::" $NODE_NAME
        echo "NODE_LABELS ::" $NODE_LABELS
        echo "WORKSPACE ::" $WORKSPACE
        echo "JENKINS_HOME ::" $JENKINS_HOME
        echo "JENKINS_URL ::" $JENKINS_URL
        echo "BUILD_URL ::" $BUILD_URL
        echo "JOB_URL ::" $JOB_URL

        String bucketName = "gs://${env.JENKINS_GCS_BUCKET}"
        String checksum = ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")

        def fileExist = sh(script: "gsutil stat ${bucketName}/npm-ci-cache-${checksum}", returnStatus: true) as Integer
        //sh "gsutil stat ${bucketName}/npm-ci-cache-${checksum}"
        //sh "gsutil cp ${bucketName}/npm-ci-cache-checksum ."
        //cacheChecksum = readFile('npm-ci-cache-checksum').trim()
        if (fileExist == 0) {
        //echo "Both values cacheChecksum: ${cacheChecksum}, checksum: ${checksum}"
        //sh "echo ${checksum} > npm-ci-cache-checksum"
    	//sh "gsutil cp npm-ci-cache-${checksum} ${bucketName}"
        //if (cacheChecksum == checksum) {
            log('DEBUG', "Cache hit! Skipping npm-ci.")
            return true
        } 
        else {
            log('DEBUG', "Cache miss! Running npm-ci.")
            sh "echo ${checksum} > npm-ci-cache-${checksum}"
	        sh "gsutil cp npm-ci-cache-${checksum} ${bucketName}"
            return false
         } 
    } catch (Exception e) {
        log('DEBUG', "Error checking cache validity: ${e}")
	    sh "gsutil cp npm-ci-cache-${checksum} ${bucketName}"
        return false
    }
}
