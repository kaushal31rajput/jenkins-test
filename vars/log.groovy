#!/usr/bin/env groovy
import com.hcl.icontrol.jenkins.Logger

/**
 * Prints log message in console depending the log level
 *
 * <pre>
    log("DEBUG", "Running with own log")
    log("INFO", "Running with own log")
    log("WARN", "Running with own log")
    log("ERROR", "Running with own log")
 * </pre>
 */
def call(String loggerLevel, def message) {
    Logger logger = new Logger(env.loggerLevel)
    if (logger.shouldLog(loggerLevel.toUpperCase())) {
        echo(logger.getFormattedMessage(loggerLevel, message))
    }
}
