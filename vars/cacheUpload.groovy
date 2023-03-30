#!/usr/bin/env groovy
import com.hcl.icontrol.jenkins.TextUtils

/**
 * Package cache dir and upload to GCS.
 */
@SuppressWarnings('unused')
def call(Map config = [:]) {
    String cacheKey = config.CACHE_KEY ?: "JOB_NAME"
    String cacheFile = TextUtils.sanitizeCacheKey("${env[cacheKey]}") + ".tar.gz"
    String bucketName = "gs://${env.JENKINS_GCS_BUCKET}"
    String workspaceCacheDir = config.WORKSPACE_CACHE_DIR
    if (!workspaceCacheDir?.trim()) {
        error("Cache dir is required!")
    }
    if (workspaceCacheDir.startsWith("/")) {
        workspaceCacheDir = workspaceCacheDir.replaceFirst("/", "")
    }
    log('DEBUG', "Cache upload cacheKey=${cacheKey}, cacheFile=${cacheFile}, workspaceCacheDir=${workspaceCacheDir}")
        dir("${env.WORKSPACE}") {
            try {
                sh script: """
                    tar -czf ${cacheFile} ${workspaceCacheDir}
                    gcloud storage cp ${cacheFile} ${bucketName}
                """
                log('DEBUG', "Cache upload finished successfully")
            } catch (Exception e) {
                log("WARN", "Cannot upload maven repository: " + e.message)
            }
    }
}
return this
