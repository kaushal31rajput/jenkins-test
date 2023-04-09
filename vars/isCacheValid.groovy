#!/usr/bin/env groovy

import com.hcl.icontrol.jenkins.ChecksumUtils

def call(Map config) {
    try {
        String checksum = config.checksum ?: ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")
        sh "gsutil stat ${bucketName}/npm-ci-cache-checksum"
        sh "gsutil cp ${bucketName}/npm-ci-cache-checksum ."
        cacheChecksum = readFile('npm-ci-cache-checksum').trim()
        echo "Both values cacheChecksum: ${cacheChecksum}, checksum: ${checksum}"
        sh "echo ${checksum} > npm-ci-cache-checksum"
    	sh "gsutil cp npm-ci-cache-checksum ${bucketName}"
        if (cacheChecksum == checksum) {
            log('DEBUG', "Cache hit! Skipping npm-ci.")
            return true
        } 
        else {
            log('DEBUG', "Cache miss! Running npm-ci.")
            return false
        } 
    } catch (Exception e) {
        log('DEBUG', "Error checking cache validity: ${e}")
        return false
    }
}
