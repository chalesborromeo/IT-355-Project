import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Rule 3; TPS04-J: Ensure ThreadLocal variables are reinitialized when using thread pools.
 * Manages user sessions with a thread pool. Since threads get recycled, we reinitialize
 * ThreadLocal variables before each task so stale session data doesnt leak between users.
 *
 * Also demonstrates Recommendation ERR52-J: Avoid in-band error indicators.
 * Instead of silently accepting invalid input or returning sentinel values, the constructor
 * throws IllegalArgumentException for bad poolSize so errors are caught immediately.
 *
 * @author Charles
 */
public class SessionThreadPool {

    /** Stores the current session ID per thread. */
    private static final ThreadLocal<String> currentSessionId = new ThreadLocal<>();

    /** Stores the current username per thread. */
    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();

    private final ExecutorService threadPool;

    /**
     * ERR52-J: throws IllegalArgumentException for invalid input instead of using
     * in-band error indicators like returning null or accepting bad values silently.
     * @param poolSize number of threads in the pool
     */
    public SessionThreadPool(int poolSize) {
        if (poolSize <= 0) {
            // ERR52-J COMPLIANT: exception instead of silent failure or sentinel value
            throw new IllegalArgumentException("Pool size must be greater than 0.");
        }
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    /**
     * Submits a session task. Reinitializes ThreadLocal variables before running
     * and cleans them up after so the next task on this thread starts fresh.
     *
     * @param sessionId the session ID for this request
     * @param username the user making the request
     * @param task the work to do
     */
    public void submitSessionTask(String sessionId, String username, Runnable task) {
        threadPool.submit(() -> {
            try {
                // TPS04-J: reinitialize ThreadLocal before each task
                currentSessionId.set(sessionId);
                currentUsername.set(username);

                System.out.println("[" + Thread.currentThread().getName()
                        + "] Session started for user: " + username
                        + " (Session: " + sessionId + ")");

                task.run();

            } finally {
                // TPS04-J: clean up so next task doesnt see stale data
                currentSessionId.remove();
                currentUsername.remove();

                System.out.println("[" + Thread.currentThread().getName()
                        + "] Session cleaned up for user: " + username);
            }
        });
    }

    /** @return current session ID for calling thread, or null */
    public static String getCurrentSessionId() {
        return currentSessionId.get();
    }

    /** @return current username for calling thread, or null */
    public static String getCurrentUsername() {
        return currentUsername.get();
    }

    /** Shuts down the thread pool. */
    public void shutdown() {
        threadPool.shutdown();
        System.out.println("Session thread pool has been shut down.");
    }
}
