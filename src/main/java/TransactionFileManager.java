import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.util.logging.*;

/**
 * Rule 2; EXP00-J: Do not ignore values returned by methods.
 * Handles transaction log file operations, always checking return values
 * from methods like File.delete() and mkdir() so failures dont go unnoticed.
 *
 * @author Charles
 */
public class TransactionFileManager {
    private static final Logger logger = Logger.getLogger(TransactionFileManager.class.getName());
    private static final String TRANSACTION_LOG_DIR = "transaction_logs";

    /**
     * Creates the transaction log directory if it doesnt exist.
     * @return true if directory was created or already exists
     */
    public boolean initializeLogDirectory() {
        File logDir = new File(TRANSACTION_LOG_DIR);

        if (logDir.exists()) {
            return true;
        }

        // EXP00-J: checking return value of mkdir()
        boolean created = logDir.mkdir();
        if (!created) {
            logger.log(Level.WARNING, "Failed to create transaction log directory.");
            return false;
        }

        System.out.println("Transaction log directory created successfully.");
        return true;
    }

    /**
     * Deletes an old transaction log file.
     * @param fileName the log file to delete
     * @return true if successfully deleted
     */
    public boolean deleteOldLog(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            System.out.println("File name cannot be null or empty.");
            return false;
        }

        File logFile = new File(TRANSACTION_LOG_DIR + "/" + fileName);

        if (!logFile.exists()) {
            System.out.println("Log file does not exist: " + fileName);
            return false;
        }

        // EXP00-J: checking return value of delete()
        boolean deleted = logFile.delete();
        if (!deleted) {
            logger.log(Level.SEVERE, "Failed to delete transaction log: " + fileName);
            System.out.println("Error: could not delete log file.");
            return false;
        }

        System.out.println("Transaction log deleted: " + fileName);
        return true;
    }

    /**
     * Exports transaction data to a file.
     * @param fileName the export file name
     * @param content the data to write
     * @return true if export was successful
     */
    public boolean exportTransactionLog(String fileName, String content) {
        if (fileName == null || content == null) {
            System.out.println("File name and content cannot be null.");
            return false;
        }

        Path filePath = Paths.get(TRANSACTION_LOG_DIR, fileName);

        try {
            // EXP00-J: capturing return value of Files.write()
            Path result = Files.write(filePath, content.getBytes());
            if (result != null) {
                System.out.println("Transaction log exported to: " + result);
                return true;
            }
        } catch (NoSuchFileException e) {
            System.err.println("Directory not found - failed to export: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Permission denied - unable to export: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error during export: " + e.getMessage());
        }

        return false;
    }

    /** Cleans up all log files. Each delete has its return value checked per EXP00-J. */
    public void cleanupAllLogs() {
        File logDir = new File(TRANSACTION_LOG_DIR);
        File[] files = logDir.listFiles();

        if (files == null) {
            System.out.println("No log files found or directory does not exist.");
            return;
        }

        for (File file : files) {
            // EXP00-J: always check return value of delete
            boolean deleted = file.delete();
            if (!deleted) {
                logger.log(Level.WARNING, "Could not delete file: " + file.getName());
            } else {
                System.out.println("Cleaned up: " + file.getName());
            }
        }
    }
}
