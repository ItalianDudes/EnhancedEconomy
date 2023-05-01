package it.italiandudes.enhancedeconomy.util;

import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public final class ServerLogger {

    // Attributes
    private static Logger logger;

    // Default Constructor
    public ServerLogger() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Init Server Logger
    public static void initLogger(final Logger logger) {
        ServerLogger.logger = logger;
    }

    // Logger Getter
    public static Logger getLogger() {
        return logger;
    }
}
