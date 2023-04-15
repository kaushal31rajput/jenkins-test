#!/usr/bin/env groovy

import com.hcl.icontrol.jenkins.ChecksumUtils

def call(Map config) {
    try {
        String bucketName = "gs://${env.JENKINS_GCS_BUCKET}"
        String checksum = ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")

        def checksumfileExist = sh(script: "gsutil stat ${bucketName}/${env.JOB_NAME}-npm-ci-cache-${checksum}", returnStatus: true) as Integer   
        def cachefileExist = sh(script: "gsutil stat ${bucketName}/${env.JOB_NAME}-npm-ci-cache.tar.gz", returnStatus: true) as Integer
        //sh "gsutil stat ${bucketName}/npm-ci-cache-${checksum}"
        //sh "gsutil cp ${bucketName}/npm-ci-cache-checksum ."
        //cacheChecksum = readFile('npm-ci-cache-checksum').trim()
        if (checksumfileExist == 0 && cachefileExist == 0) {
        //echo "Both values cacheChecksum: ${cacheChecksum}, checksum: ${checksum}"
        //sh "echo ${checksum} > npm-ci-cache-checksum"
    	//sh "gsutil cp npm-ci-cache-${checksum} ${bucketName}"
        //if (cacheChecksum == checksum) {
            log('DEBUG', "Cache hit! Skipping npm-ci.")
            return true
        } 
        else {
            log('DEBUG', "Cache miss! Running npm-ci.")
            sh "echo ${checksum} > ${env.JOB_NAME}-npm-ci-cache-${checksum}"
	        sh "gsutil cp ${env.JOB_NAME}-npm-ci-cache-${checksum} ${bucketName}"
            return false
         } 
    } catch (Exception e) {
        log('DEBUG', "Error checking cache validity: ${e}")
	    sh "gsutil cp ${env.JOB_NAME}-npm-ci-cache-${checksum} ${bucketName}"
        return false
    }
}
