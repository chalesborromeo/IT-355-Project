/**
 * Rule 5; TSM03-J: Do not publish partially initialized objects.
 * Uses volatile and synchronized to safely share account objects between threads.
 * Without this, other threads could see fields with default values (null, 0)
 * instead of their actual assigned values.
 *
 * @author Charles
 */
public class AccountPublisher {

    /** TSM03-J: volatile ensures all threads see the fully initialized object. */
    private volatile Account publishedAccount;

    /** Lock for safely publishing/reading the account across threads. */
    private final Object publishLock = new Object();

    /**
     * Publishes an account safely using synchronization.
     * TSM03-J: synchronized block guarantees all fields are visible to other threads.
     *
     * @param account the fully constructed account to publish
     */
    public void publishAccount(Account account) {
        if (account == null) {
            System.out.println("Cannot publish a null account.");
            return;
        }

        // TSM03-J: synchronized to safely publish the object
        synchronized (publishLock) {
            this.publishedAccount = account;
            System.out.println("Account published safely: " + account.getAccountName());
        }
    }

    /**
     * Gets the published account in a thread-safe way.
     * @return the published account or null if none published
     */
    public Account getPublishedAccount() {
        synchronized (publishLock) {
            return publishedAccount;
        }
    }

    /**
     * Creates and publishes an account safely.
     * @param accountName the account name
     * @param balance the starting balance
     */
    public void createAndPublishAccount(String accountName, double balance) {
        Account newAccount = new Account(accountName, balance);
        // TSM03-J: only publish after construction is fully done
        publishAccount(newAccount);
    }

    /*
     * NON-COMPLIANT example: without synchronized or volatile, thread B might
     * read publishedAccount and see accountName=null, balance=0.0 even though
     * thread A already set them. The Java Memory Model doesnt guarantee visibility
     * without proper synchronization.
     */
}
