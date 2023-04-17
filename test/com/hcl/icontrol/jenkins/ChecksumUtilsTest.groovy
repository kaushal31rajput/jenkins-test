package com.hcl.icontrol.jenkins

import spock.lang.Specification

class ChecksumUtilsTest extends Specification {

    def "getChecksum should calculate the correct checksum for the given files"() {
        given:
        def expectedChecksum = "439329280"
        def file1Contents = "Hello"
        def file2Contents = "World"
        def file1 = File.createTempFile("file1", ".txt")
        def file2 = File.createTempFile("file2", ".txt")
        file1 << file1Contents
        file2 << file2Contents

        when:
        def actualChecksum = ChecksumUtils.getChecksum(file1.absolutePath, file2.absolutePath)

        then:
        actualChecksum == expectedChecksum

        cleanup:
        file1.delete()
        file2.delete()

    }
}

