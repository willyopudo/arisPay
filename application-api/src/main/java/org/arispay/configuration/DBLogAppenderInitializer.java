package org.arispay.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Initializes the Log4j2 JDBC database appender after Spring application has fully started.
 * This ensures database credentials are available when the appender needs them.
 */
@Component
public class DBLogAppenderInitializer {

    /**
     * Enable the DBLogsStore appender in all loggers after Spring has initialized.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void enableDBAppender() {
        try {
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);

            // Add appender ref to root logger
            LoggerConfig rootLogger = loggerContext.getConfiguration().getRootLogger();
            rootLogger.addAppender(loggerContext.getConfiguration().getAppender("DBLogsStore"), null, null);

            // Add appender ref to org.arispay logger
            LoggerConfig arisPayLogger = loggerContext.getConfiguration().getLoggerConfig("org.arispay");
            if (arisPayLogger != null) {
                arisPayLogger.addAppender(loggerContext.getConfiguration().getAppender("DBLogsStore"), null, null);
            }

            // Update the context configuration
            loggerContext.updateLoggers();

            org.slf4j.LoggerFactory.getLogger(DBLogAppenderInitializer.class)
                .info("Database log appender (DBLogsStore) enabled successfully");
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(DBLogAppenderInitializer.class)
                .error("Failed to enable database log appender", e);
        }
    }
}
