package com.hcl.icontrol.jenkins.support.external

/**
 * An exception class to exit a stage due to the when statement
 * Code extracted from https://github.com/macg33zr/pipelineUnit
 */
class WhenExitException extends Exception {

	WhenExitException(String message) {
		super(message);
	}
}
