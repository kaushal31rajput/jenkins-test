package com.hcl.icontrol.jenkins

class Logger {

    private static DEBUG = "DEBUG"
    private static INFO = "INFO"
    private static WARN = "WARN"
    private static ERROR = "ERROR"

    private static final BLACK = '\033[0;30m'
    private static final GREEN = '\033[0;42m'
    private static final YELLOW = '\033[0;43m'
    private static final RED = '\033[1;31m'
    private static final NC = '\033[0m'

    private static final Map<String, String> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put(DEBUG, BLACK)
        COLOR_MAP.put(INFO, GREEN)
        COLOR_MAP.put(WARN, YELLOW)
        COLOR_MAP.put(ERROR, RED)
    }

    def loggerLevelCfg = DEBUG
    def appContext

    Logger(Object loggerLevelParam) {
        super()
        this.loggerLevelCfg = loggerLevelParam != null ? loggerLevelParam : DEBUG
    }

    Logger(Object loggerLevelParam, def appContext) {
        super()
        this.loggerLevelCfg = loggerLevelParam != null ? loggerLevelParam : DEBUG
        this.appContext = appContext
    }

    Logger() {
        super()
    }

    boolean shouldLog(String loggerLevel) {
        if (loggerLevelCfg == DEBUG) {
            return true
        } else if (INFO.equalsIgnoreCase(loggerLevelCfg) && (INFO.equalsIgnoreCase(loggerLevel) || ERROR.equalsIgnoreCase(loggerLevel))) {
            return true
        } else if (ERROR.equalsIgnoreCase(loggerLevelCfg) && ERROR.equalsIgnoreCase(loggerLevel)) {
            return true
        }
        return false
    }

    void log(String loggerLevel, def message) {
        if (shouldLog(loggerLevel)) {
            if (appContext != null) {
                appContext.echo(getFormattedMessage(loggerLevel, message))
            }
        }
    }

    String getFormattedMessage(String loggerLevel, def message) {
        return COLOR_MAP.get(loggerLevel, DEBUG) + " [" + loggerLevel + "] " + message + " " + NC
    }
}
