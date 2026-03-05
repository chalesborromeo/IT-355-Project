import java.util.ArrayList;
import java.util.List;

/**
 * Rule 4; TSM01-J: Do not let the this reference escape during object construction.
 * Builds account objects safely by keeping "this" contained within the constructor.
 * We dont start threads, register listeners, or store this publicly during construction.
 *
 * Also demonstrates Recommendation OBJ50-J: Never confuse the immutability of a
 * reference with that of the referenced object. The final keyword on permissions only
 * makes the reference immutable, not the list contents, so we return defensive copies.
 *
 * @author Charles
 */
public class SafeAccountBuilder {

    private final String accountName;
    private final double initialBalance;
    private final String accountType;
    private final List<String> permissions;

    /**
     * TSM01-J: this reference does not escape. All fields are set before
     * the constructor finishes and no external code can see this object mid-build.
     *
     * @param accountName the account name
     * @param initialBalance the starting balance
     * @param accountType checking or savings
     */
    public SafeAccountBuilder(String accountName, double initialBalance, String accountType) {
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be null or empty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null.");
        }

        this.accountName = accountName;
        this.initialBalance = initialBalance;
        this.accountType = accountType;

        // TSM01-J: list created entirely within the constructor, this doesnt escape
        this.permissions = new ArrayList<>();
        initializePermissions();
    }

    /**
     * Private so subclasses cant override it. Safe to call from constructor.
     */
    private void initializePermissions() {
        permissions.add("VIEW_BALANCE");
        permissions.add("DEPOSIT");

        if ("Checking".equalsIgnoreCase(accountType)) {
            permissions.add("WITHDRAW");
            permissions.add("TRANSFER");
        } else if ("Savings".equalsIgnoreCase(accountType)) {
            permissions.add("WITHDRAW");
        }
    }

    /** @return the account name */
    public String getAccountName() {
        return accountName;
    }

    /** @return the initial balance */
    public double getInitialBalance() {
        return initialBalance;
    }

    /** @return the account type */
    public String getAccountType() {
        return accountType;
    }

    /**
     * OBJ50-J: permissions is declared final, but that only makes the reference
     * immutable, not the list itself. We return a defensive copy so external code
     * cannot mutate the internal list through the returned reference.
     * @return a copy of the permissions list
     */
    public List<String> getPermissions() {
        // OBJ50-J COMPLIANT: defensive copy because final != immutable contents
        return new ArrayList<>(permissions);
    }

    /*
     * NON-COMPLIANT example (dont do this):
     * public SafeAccountBuilder(String name) {
     *     this.accountName = name;
     *     new Thread(() -> registerAccount(this)).start(); // BAD - this escapes
     *     this.initialBalance = 0; // thread sees half-built object
     * }
     */
}
