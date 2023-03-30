package com.hcl.icontrol.jenkins

class ChecksumUtils {
    static String getChecksum(String... fileNames) {
        def fileContents = ''
        fileNames.each { fileName ->
            fileContents += new File(fileName).text
        }
        return fileContents.hashCode().toString().trim()
    }
}
