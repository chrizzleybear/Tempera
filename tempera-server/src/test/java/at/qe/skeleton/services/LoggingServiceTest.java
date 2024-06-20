package at.qe.skeleton.services;

import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggingServiceTest {

    private static final String LOG_DIR_PATH = File.separator + "logs";
    private static final String LOG_WARNING_PATH = LOG_DIR_PATH + File.separator + "warning.log";
    private static final String LOG_INFO_PATH = LOG_DIR_PATH + File.separator + "info.log";

    private LoggingService loggingService;

    @BeforeEach
    public void setUp() {
        loggingService = new LoggingService();
        deleteFiles();
    }

    @AfterEach
    public void tearDown() {
        deleteFiles();
    }

    private void deleteFiles() {
        try {
            Files.deleteIfExists(Paths.get(LOG_WARNING_PATH));
            Files.deleteIfExists(Paths.get(LOG_INFO_PATH));
            Files.deleteIfExists(Paths.get(LOG_DIR_PATH));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testLogEventCreatesFiles() {
        loggingService.logEvent(LogEvent.CREATE, LogAffectedType.USER, "Test info message");

        assertTrue(Files.exists(Paths.get(LOG_INFO_PATH)));
        assertTrue(Files.exists(Paths.get(LOG_WARNING_PATH)));
    }

    @Test
    public void testLogInfoMessage() throws IOException {
        String message = "Test info message";
        loggingService.logEvent(LogEvent.CREATE, LogAffectedType.USER, message);

        assertTrue(Files.exists(Paths.get(LOG_INFO_PATH)));
        assertLogFileContainsMessage(LOG_INFO_PATH, LogEvent.CREATE, LogAffectedType.USER, message);
    }

    @Test
    public void testLogWarningMessage() throws IOException {
        String message = "Test warning message";
        loggingService.logEvent(LogEvent.WARN, LogAffectedType.GROUP, message);

        assertTrue(Files.exists(Paths.get(LOG_WARNING_PATH)));
        assertLogFileContainsMessage(LOG_WARNING_PATH, LogEvent.WARN, LogAffectedType.GROUP, message);
    }

    private void assertLogFileContainsMessage(String logFilePath, LogEvent actionType, LogAffectedType affectedType, String message) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            String expectedLogEntry = getExpectedLogEntry(actionType, affectedType, message);
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(expectedLogEntry)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Log file does not contain the expected message: " + expectedLogEntry);
        }
    }

    private String getExpectedLogEntry(LogEvent actionType, LogAffectedType affectedType, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return timestamp + " - " + actionType + " - " + affectedType + " - " + message;
    }
}

