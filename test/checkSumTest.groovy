import com.hcl.icontrol.jenkins.support.JenkinsPipelineSharedLibTemplateTest

class checkSumTest extends JenkinsPipelineSharedLibTemplateTest {
    @Override
    String getScriptName() {
        return "vars/checkSum.groovy"
    }

    def "getChecksum should calculate the correct checksum for the given files"() {
        given:
        //def expectedChecksum = "439329280"
        def expectedChecksum = "439329280"
        def file1Contents = "Hello"
        def file2Contents = "World"
        def file1 = new File(env.WORKSPACE, "file1.txt")
        def file2 = new File(env.WORKSPACE, "file2.txt")
        file1 << file1Contents
        file2 << file2Contents
        println "content of ${file1} file2"

        when:
        def filenames = [file1: file1.absolutePath, file2: file2.absolutePath]
        def actualChecksum = callScript(loadScript(getScriptName()), filenames)

        then:
        actualChecksum == expectedChecksum

        cleanup:
        file1.delete()
        file2.delete()
    }
}


    // def "getChecksum should calculate the correct checksum for the given files"() {
    //     given:
    //     def expectedChecksum = "439329280"
    //     def file1Contents = "Hello"
    //     def file2Contents = "World"
    //     def file1 = File.createTempFile("file1", ".txt")
    //     def file2 = File.createTempFile("file2", ".txt")
    //     file1 << file1Contents
    //     file2 << file2Contents
    //     println "content of ${file1} file2"

    //     when:
    //     def filenames = [file1: file1.absolutePath, file2: file2.absolutePath]
    //     //def actualChecksum = script.call(file1.absolutePath, file2.absolutePath)
    //     //def script = loadScript(getScriptName())
    //     //callScript(loadScript(getScriptName()) , ("${file1}", "${file2}"))
    //     println file1.absolutePath 
    //     println "content of ${file1} file2"
    //     def actualChecksum = callScript(loadScript(getScriptName()), filenames)
    //     //def actualChecksum = callScript(loadScript(getScriptName()), [file1: '${file1}', file2: '${file2}'])
    //     //def actualChecksum = callScript(loadScript(getScriptName()), "${file1}", "${file2}")
    //     println "afaq1"
    //     println actualChecksum
    //     println "afaq2"
    //     //def actualChecksum = evaluate(script, (file1.absolutePath, file2.absolutePath))

    //     then:
    //     actualChecksum == expectedChecksum

    //     cleanup:
    //     file1.delete()
    //     file2.delete()
    // }
//}