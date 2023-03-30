#!/usr/bin/env groovy


def call(Map config) {
    String nodeModulesDir = "${env.WORKSPACE}/node_modules"
    String packageJson = readFile("${env.WORKSPACE}/package.json")
    String packageLockJson = readFile("${env.WORKSPACE}/package-lock.json")
    String checksum = getChecksum(packageJson, packageLockJson)
    String cacheKey = "npm-ci-cache-${checksum}"
    String bucketName = "gs://my-new-bucket-12344321-kaushal"

    if (isCacheValid(cacheKey, bucketName, packageJson, packageLockJson)) {
        echo "Restoring node_modules from cache"
        restoreFromCache(cacheKey, bucketName, nodeModulesDir)
    } else {
        echo "Installing npm dependencies"
        sh "npm ci"
        echo "Caching node_modules"
        cache(nodeModulesDir, cacheKey, bucketName, checksum)
    }
}

def isCacheValid(cacheKey, bucketName, packageJson, packageLockJson) {
    def checksum = getChecksum(packageJson, packageLockJson)
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


def restoreFromCache(cacheKey, bucketName, nodeModulesDir) {
    try {
        sh "gsutil stat ${bucketName}/${cacheKey}.tar.gz"
        sh "gsutil cp ${bucketName}/${cacheKey}.tar.gz ."
        sh "tar -zxvf ${cacheKey}.tar.gz"
        sh "rm ${cacheKey}.tar.gz"
    } catch (Exception e) {
        echo "Cache not found in GCS bucket, installing dependencies"
        sh "npm ci"
    }
 }


def cache(path, key, bucketName, checksum) {
    try {
	sh "pwd"
	sh "ls -larth"
        sh "tar -czf ${key}.tar.gz node_modules"
        sh "gsutil cp ${key}.tar.gz ${bucketName}"
        sh "rm ${key}.tar.gz"
        sh "echo ${checksum} > npm-ci-cache-checksum"
	  sh "gsutil cp npm-ci-cache-checksum ${bucketName}"
    } catch (Exception e) {
        error "Failed to cache ${path}"
    }
  }

def getChecksum(packageJson, packageLockJson) {
    def checksum = "${packageJson}${packageLockJson}".hashCode()
    return checksum.toString().trim()
}


def fileExists(path) {
    return new File(path).exists()
}

