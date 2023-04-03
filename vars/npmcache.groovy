#!/usr/bin/env groovy

import com.hcl.icontrol.jenkins.ChecksumUtils

def call(Map config) {
    String nodeModulesDir = "${env.WORKSPACE}/node_modules"
    //String checksum = ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")
    String checksum = config.checksum ?: ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")
    String cacheKey = "npm-ci-cache-${checksum}"
    String bucketName = "gs://${env.JENKINS_GCS_BUCKET}"

    if (isCacheValid(bucketName, checksum)) {
        log('DEBUG', "Restoring node_modules from cache")
        restoreFromCache(bucketName)
    } else {
        log('DEBUG', "Installing npm dependencies")
        sh "npm ci"
        log('DEBUG', "Caching node_modules")
        cache(cacheKey, bucketName, checksum)
        log('DEBUG', "Caching pushed to GCS bucket")
    }
}

def isCacheValid(bucketName, checksum) {
    try {
        sh "gsutil stat ${bucketName}/npm-ci-cache-checksum"
        sh "gsutil cp ${bucketName}/npm-ci-cache-checksum ."
        cacheChecksum = readFile('npm-ci-cache-checksum').trim()
        echo "Both values cacheChecksum: ${cacheChecksum}, checksum: ${checksum}"
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


def restoreFromCache(bucketName) {
    try {
        cacheDownload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "npm-ci-cache"])
    } catch (Exception e) {
        log('DEBUG', "Cache not found in GCS bucket, installing dependencies")
        sh "npm ci"
    }
}


def cache(key, bucketName, checksum) {
    try {
	sh "pwd"
	sh "ls -larth"
    sh "echo ${checksum} > npm-ci-cache-checksum"
    sh "gsutil cp npm-ci-cache-checksum ${bucketName}"
    cacheUpload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "npm-ci-cache"])
    } catch (Exception e) {
        echo "Error caching  to ${bucketName}/${key}: ${e}"

    }
}

// def fileExists(path) {
//     return new File(path).exists()
// }
