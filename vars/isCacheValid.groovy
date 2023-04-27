#!/usr/bin/env groovy

def call(Map config) {
    container('gcloud') {
    try {
        String bucketName = "gs://${env.JENKINS_GCS_BUCKET}"
        String checksum = config.checksum ?: checkSum(file1: "${config.file1}", file2: "${config.file2}")
        String job = "${env.JOB_NAME}".contains("/") ? "${env.JOB_NAME}".replaceAll("/", "-") : "${env.JOB_NAME}"

        def checksumfileExist = sh(script: "gsutil stat ${bucketName}/${job}-${config.NAME}-${checksum}", returnStatus: true) as Integer   
        def cachefileExist = sh(script: "gsutil stat ${bucketName}/${job}-${config.NAME}.tar.gz", returnStatus: true) as Integer
        if (checksumfileExist == 0 && cachefileExist == 0) {
            log('DEBUG', "Cache hit! Skipping npm-ci.")
            return true
        } 
        else {
            log('DEBUG', "Cache miss! Running npm-ci.")
            sh "echo ${checksum} > ${job}-${config.NAME}-${checksum}"
	        sh "gsutil cp ${job}-${config.NAME}-${checksum} ${bucketName}"
            return false
         } 
    } catch (Exception e) {
        log('DEBUG', "Error checking cache validity: ${e}")
	    sh "gsutil cp ${job}-${config.NAME}-${checksum} ${bucketName}"
        return false
    }
 }
}
