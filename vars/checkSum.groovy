#!/usr/bin/env groovy

def call(Map fileNames) {
    def fileContents = ''
    fileNames.each { fileName, filePath ->
        println "Reading file ${filePath}"
        fileContents += readFile(filePath)
        println "File contents so far: ${fileContents}"
    }
    println "Final file contents: ${fileContents}"
    return fileContents.hashCode().toString().trim()
}





// def call(Map fileNames) {
//          def fileContents = ''
//          fileNames.each {
//             fileContents += readFile(it.value)
//          }
//          return fileContents.hashCode().toString().trim()
// }
 
// def call(String... fileNames) {
//          def fileContents = ''
//          fileNames.each { fileName ->
//             fileContents += readFile(fileName)
//          }
//          return fileContents.hashCode().toString().trim()
// }