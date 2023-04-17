package com.hcl.icontrol.jenkins.support


import com.lesfurets.jenkins.unit.RegressionTest
import spock.lang.Specification

/**
 * A base class for Spock testing using the pipeline helper
 *
 * Code extracted from https://github.com/macg33zr/pipelineUnit
 */
abstract class JenkinsPipelineBaseTest extends Specification implements RegressionTest {

    /**
     * Delegate to the test helper
     */
    @Delegate
    JenkinsPipelineTestHelper jenkinsPipelineTestHelper

    /**
     * Do the common setup
     */
    def setup() {
        // Set callstacks path for RegressionTest
        callStackPath = 'test/callstacks/'

        // Create and config the helper
        jenkinsPipelineTestHelper = new JenkinsPipelineTestHelper()
        jenkinsPipelineTestHelper.setUp()
    }

}
