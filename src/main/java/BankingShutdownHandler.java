import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Rule 6; FIO14-J: Perform proper cleanup at program termination.
 * Registers a shutdown hook so cleanup runs even if the app exits unexpectedly.
 *
 * Also demonstrates Recommendation FIO50-J: Do not make assumptions about file
 * creation. The shutdown log write handles IOException rather than assuming the
 * file or directory will always exist and be writable.
 *
 * @author Charles
 */
public class BankingShutdownHandler {

    private static final String SHUTDOWN_LOG = "transaction_logs/shutdown_log.txt";
    private final SessionThreadPool sessionPool;

    /**
     * FIO14-J: registers a shutdown hook with the JVM for cleanup on exit.
     * @param sessionPool the thread pool to shut down on exit
     */
    public BankingShutdownHandler(SessionThreadPool sessionPool) {
        this.sessionPool = sessionPool;

        // FIO14-J: shutdown hook runs cleanup even on unexpected termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            performCleanup();
        }));

        System.out.println("Shutdown handler registered.");
    }

    /** FIO14-J: cleanup logic - shuts down thread pool and writes log. */
    private void performCleanup() {
        System.out.println("Banking app shutting down... performing cleanup.");

        if (sessionPool != null) {
            sessionPool.shutdown();
        }

        writeShutdownLog();
        System.out.println("Cleanup complete. Goodbye.");
    }

    /**
     * Writes a shutdown log entry.
     * FIO50-J: does not assume the log file or directory exists. Catches
     * IOException in case the path is invalid or not writable.
     */
    private void writeShutdownLog() {
        // FIO50-J: no assumption that the file/directory exists or is writable
        try (FileWriter writer = new FileWriter(SHUTDOWN_LOG, true)) {
            writer.write("Banking app shutdown at: " + LocalDateTime.now() + "\n");
            System.out.println("Shutdown log written successfully.");
        } catch (IOException e) {
            System.err.println("Failed to write shutdown log: " + e.getMessage());
        }
    }

    /** Can be called manually for a clean user-initiated shutdown. */
    public void handleGracefulShutdown() {
        System.out.println("User initiated shutdown.");
        performCleanup();
    }
}
