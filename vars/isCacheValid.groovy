#!/usr/bin/env groovy

import com.hcl.icontrol.jenkins.ChecksumUtils

def call(Map config) {
    try {
        String bucketName = "gs://${env.JENKINS_GCS_BUCKET}"
        String checksum = ChecksumUtils.getChecksum("${env.WORKSPACE}/${config.file1}", "${env.WORKSPACE}/${config.file2}")
        log('DEBUG', "checksum: ${checksum}")

        def checksumfileExist = sh(script: "gsutil stat ${bucketName}/${env.JOB_NAME}-config.NAME-${checksum}", returnStatus: true) as Integer   
        def cachefileExist = sh(script: "gsutil stat ${bucketName}/${env.JOB_NAME}-config.NAME.tar.gz", returnStatus: true) as Integer
        if (checksumfileExist == 0 && cachefileExist == 0) {
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
