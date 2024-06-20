package at.qe.skeleton.services;

import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingService {

    // Log file paths
    private static final String LOG_DIR_PATH = Paths.get("logs").toString();
    private static final String LOG_WARNING_NAME = "warning.log";
    private static final String LOG_INFO_NAME = "info.log";
    private static final String LOG_WARNING_PATH = LOG_DIR_PATH + "/" + LOG_WARNING_NAME;
    private static final String LOG_INFO_PATH = LOG_DIR_PATH + "/" + LOG_INFO_NAME;
    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);

    private File warningLogFile;
    private File infoLogFile;

    public LoggingService() {
        createFiles();
    }

    private void createFiles() {
        try {
            File logDir = new File(LOG_DIR_PATH);
            if (!logDir.exists()) {
                if (logDir.mkdirs()) {
                    log.info("Log directory created: " + LOG_DIR_PATH);
                }
            }
            warningLogFile = new File(LOG_WARNING_PATH);
            if (!warningLogFile.exists()) {
                if (warningLogFile.createNewFile()) {
                   log.info("Warning log file created: " + LOG_WARNING_PATH);
                }
            }
            infoLogFile = new File(LOG_INFO_PATH);
            if (!infoLogFile.exists()) {
                if (infoLogFile.createNewFile()) {
                    log.info("Info log file created: " + LOG_INFO_PATH);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to create log files: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Failed to create log files due to permissions: " + e.getMessage());
        }
    }

    /**
     * Logs a message to the log file, if the file does not exist it's created.
     *
     * @param actionType  The type of log event.
     * @param affectedType The type of affected entity.
     * @param message The message to be logged.
     */
    public void logEvent(LogEvent actionType, LogAffectedType affectedType, String message) {
        if (!warningLogFile.exists() || !infoLogFile.exists()) {
            createFiles();
        }
        // Determine which log file to write to
        boolean severe = switch (actionType) {
            case WARN, ERROR -> true;
            default -> false;
        };
        File logFile = severe ? warningLogFile : infoLogFile;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + " - " + actionType + " - " + affectedType + " - " + message);
            writer.newLine();
        } catch (IOException e) {
            log.error("Failed to write to log file: " + e.getMessage());
        }
    }
}
