/**
 * Rule 2; THI00-J: Do not invoke Thread.run().
 * Processes transactions in the background. We use thread.start() instead of
 * thread.run() so the work actually runs on a new thread instead of blocking
 * the current one.
 *
 * @author Charles
 */
public class TransactionThread implements Runnable {
    private final String accountName;
    private final double amount;
    private final String transactionType;

    /**
     * @param accountName the account being processed
     * @param amount the transaction amount
     * @param transactionType "DEPOSIT" or "WITHDRAWAL"
     */
    public TransactionThread(String accountName, double amount, String transactionType) {
        this.accountName = accountName;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    /** Contains the transaction processing logic that runs on the new thread. */
    @Override
    public void run() {
        System.out.println("[Thread: " + Thread.currentThread().getName() + "] Processing "
                + transactionType + " of $" + amount + " for account: " + accountName);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Transaction processing was interrupted for: " + accountName);
            return;
        }

        System.out.println("[Thread: " + Thread.currentThread().getName() + "] "
                + transactionType + " of $" + amount + " completed for: " + accountName);
    }

    /**
     * Starts the transaction on a new thread.
     * THI00-J: uses start() not run() so it actually runs in parallel.
     */
    public void processInBackground() {
        Thread transactionWorker = new Thread(this);
        // THI00-J COMPLIANT: start() creates a real new thread
        transactionWorker.start();
    }

    /**
     * Shows the wrong approach for reference (non-compliant).
     * Calling run() directly would just run on the current thread.
     */
    public void processIncorrectly() {
        Thread transactionWorker = new Thread(this);
        // THI00-J NON-COMPLIANT example:
        // transactionWorker.run(); // BAD - does not create a new thread
        transactionWorker.start(); // always use start() instead
    }
}
