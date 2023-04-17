package com.hcl.icontrol.jenkins.support

abstract class JenkinsPipelineSharedLibTemplateTest extends JenkinsPipelineBaseTest {

    abstract String getScriptName()

    Map prepareParams() {
        return [:]
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    protected boolean pipelineStackWrite() {
        return false
    }

    def  "test non regression"() {
        given:
        if (pipelineStackWrite()) {
            System.getProperties().setProperty("pipeline.stack.write", "true")
        }
        Map params = prepareParams()

        when:
        def result = callScript(loadScript(getScriptName()) , params)

        then:
        extraAssertions(result)
        printCallStack()

        if (isJobStatusSuccess()) {
            assertJobStatusSuccess()
        } else {
            assertJobStatusFailure()
        }

        then:
        if (!skipNonRegression()) {
            testNonRegression("should_complete_with_success")
        }
    }

    boolean skipNonRegression() {
        return false
    }

    void extraAssertions(def result) {
    }

    boolean isJobStatusSuccess() {
        return true
    }

    def callScript(def script, Map params) {
        return script.call(params)
    }

}
