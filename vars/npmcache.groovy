#!/usr/bin/env groovy

import com.hcl.icontrol.jenkins.ChecksumUtils

def call(Map config) {
    String nodeModulesDir = "${env.WORKSPACE}/node_modules"
    //String packageJson = readFile("${env.WORKSPACE}/package.json")
    //String packageLockJson = readFile("${env.WORKSPACE}/package-lock.json")
    String checksum = ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")
    String cacheKey = "npm-ci-cache-${checksum}"
    String bucketName = "gs://my-new-bucket-12344321-kaushal"

    if (isCacheValid(bucketName, checksum)) {
        echo "Restoring node_modules from cache"
        restoreFromCache(bucketName)
    } else {
        echo "Installing npm dependencies"
        sh "npm ci"
        echo "Caching node_modules"
        cache(cacheKey, bucketName, checksum)
    }
}

def isCacheValid(bucketName, checksum) {
   //def checksum = getChecksum(packageJson, packageLockJson)
   //def checksum = ChecksumUtils.getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json")
    try {
        sh "gsutil stat ${bucketName}/npm-ci-cache-checksum"
        sh "gsutil cp ${bucketName}/npm-ci-cache-checksum ."
        cacheChecksum = readFile('npm-ci-cache-checksum').trim()
        echo "Both values cacheChecksum: ${cacheChecksum}, checksum: ${checksum}"
        if (cacheChecksum == checksum) {
            println "Cache hit! Skipping npm-ci."
            return true
        } 
        else {
            println "Cache miss! Running npm-ci."
            return false
        } 
    } catch (Exception e) {
        println "Error checking cache validity: ${e}"
        return false
    }
  }


def restoreFromCache(bucketName) {
    try {
         cacheDownload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "npm-ci-cache"])
        //sh "gsutil stat ${bucketName}/${cacheKey}.tar.gz"
        //sh "gsutil cp ${bucketName}/${cacheKey}.tar.gz ."
        //sh "tar -zxvf ${cacheKey}.tar.gz"
        //sh "rm ${cacheKey}.tar.gz"
    } catch (Exception e) {
        echo "Cache not found in GCS bucket, installing dependencies"
        sh "npm ci"
    }
 }

def cache(key, bucketName, checksum) {
    try {
        sh "pwd"
        sh "ls -larth"
        //sh "tar -czf ${key}.tar.gz node_modules"
	 sh "echo ${checksum} > npm-ci-cache-checksum"
         sh "gsutil cp npm-ci-cache-checksum ${bucketName}"
         cacheUpload([WORKSPACE_CACHE_DIR: "node_modules", CACHE_KEY: "npm-ci-cache"])
	//cacheUpload([WORKSPACE_CACHE_DIR: ".jest/cache", CACHE_KEY: "JEST_CACHE_KEY_NAME"])
	//CacheUpload(config: [CACHE_KEY: key, WORKSPACE_CACHE_DIR: '.', JENKINS_GCS_BUCKET: bucketName]).call()

    } catch (Exception e) {
        error "Failed to cache ${path}"
    }
  }

//def getChecksum(packageJson, packageLockJson) {
//    def checksum = "${packageJson}${packageLockJson}".hashCode()
//    return checksum.toString().trim()
//}


def fileExists(path) {
    return new File(path).exists()
}

