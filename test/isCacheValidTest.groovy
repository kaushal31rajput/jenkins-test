import com.hcl.icontrol.jenkins.ChecksumUtils
import groovy.mock.interceptor.MockFor
import spock.lang.Specification

class IsCacheValidSpec extends Specification {

    def 'should return true for cache hit'() {
        given:
        def env = [
            WORKSPACE: 'test-workspace',
            JENKINS_GCS_BUCKET: 'test-bucket-npmcache',
            JOB_NAME: 'icontrol'
        ]
        def checksum = '12345'
        def checksumfileExist = 0
        def cachefileExist = 0
        def mUtilsMock = new MockFor(ChecksumUtils)
        mUtilsMock.demand.getChecksum { String file1, String file2 -> checksum }
        def isCacheValid = IsCacheValid(delegate: [env: env, checksumUtils: mUtilsMock.proxy()])

        when:
        def result = isCacheValid(config: env)

        then:
        result == true
        mUtilsMock.verify(getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json"), 1)
        verify(sh("gsutil stat gs://${env.JENKINS_GCS_BUCKET}/${env.JOB_NAME}-npm-cache-${checksum}"), 1)
        verify(sh("gsutil stat gs://${env.JENKINS_GCS_BUCKET}/${env.JOB_NAME}-npm-cache.tar.gz"), 1)
        verify(sh("echo ${checksum} > ${env.JOB_NAME}-npm-cache-${checksum}"), 0)
        verify(sh("gsutil cp ${env.JOB_NAME}-npm-cache-${checksum} gs://${env.JENKINS_GCS_BUCKET}"), 0)
    }

    def 'should return false for cache miss'() {
        given:
        def env = [
            WORKSPACE: 'test-workspace',
            JENKINS_GCS_BUCKET: 'test-bucket-npmcache',
            JOB_NAME: 'icontrol'
        ]
        def checksum = '12345'
        def checksumfileExist = 1
        def cachefileExist = 1
        def mUtilsMock = new MockFor(ChecksumUtils)
        mUtilsMock.demand.getChecksum { String file1, String file2 -> checksum }
        def isCacheValid = IsCacheValid(delegate: [env: env, checksumUtils: mUtilsMock.proxy()])

        when:
        def result = isCacheValid(config: env)

        then:
        result == false
        mUtilsMock.verify(getChecksum("${env.WORKSPACE}/package.json", "${env.WORKSPACE}/package-lock.json"), 1)
        verify(sh("gsutil stat gs://${env.JENKINS_GCS_BUCKET}/${env.JOB_NAME}-npm-cache-${checksum}"), 1)
        verify(sh("gsutil stat gs://${env.JENKINS_GCS_BUCKET}/${env.JOB_NAME}-npm-cache.tar.gz"), 1)
        verify(sh("echo ${checksum} > ${env.JOB_NAME}-npm-cache-${checksum}"), 1)
        verify(sh("gsutil cp ${env.JOB_NAME}-npm-cache-${checksum} gs://${env.JENKINS_GCS_BUCKET}"), 1)
    }
}
