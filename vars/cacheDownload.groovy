#!/usr/bin/env groovy
import com.hcl.icontrol.jenkins.TextUtils

/**
 * Download zipped cache and extract.
 */
@SuppressWarnings('unused')
def call(Map config = [:]) {
    String cacheKey = config.CACHE_KEY ?: "JOB_NAME"
    log('DEBUG', "${env[cacheKey]}")
    String cacheFile = TextUtils.sanitizeCacheKey("${env[cacheKey]}") + ".tar.gz"
    String bucketName = "gs://${env.JENKINS_GCS_BUCKET}"
    String workspaceCacheDir = config.WORKSPACE_CACHE_DIR
    if (!workspaceCacheDir?.trim()) {
        error("Cache dir is required!")
    }
    if (workspaceCacheDir.startsWith("/")) {
        workspaceCacheDir = workspaceCacheDir.replaceFirst("/", "")
    }

    log('DEBUG', "Download cache cacheKey=${cacheKey}, cache file=${cacheFile}, directory=${workspaceCacheDir},")
        def fileExist = sh(script: "gcloud storage ls -L ${bucketName}/${cacheFile}", returnStatus: true) as Integer
        if (fileExist == 0) {
            dir("${env.WORKSPACE}") {
                sh script: """
                    gcloud storage cp ${bucketName}/${cacheFile} ${cacheFile}
                    mkdir -p ${workspaceCacheDir}
                    tar -xf ${cacheFile}
                    rm ${cacheFile}
                """
            }
        }
}

return this
