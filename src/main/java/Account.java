import java.time.LocalDateTime;
import java.util.Arrays;
/*
* OBJ10-J "Don’t use public static nonfinal variables" is followed in this file, no public static nonfinal variables are present
* MET01-J "Don’t use assertions in the final code" is followed in this file, no assertations are present
*/
public class Account {
    
    private String accountName;
    private double balance;
    private Transaction[] transactionHistory;
    private static final int MAX_TRANSACTIONS = 100;

    //Rule 9 - LCK01-J - Devin Diaz
    //Private lock object prevents synchronization on reusable objects
    private final Object lock = new Object();

    /** 
    * Default constructor
    */
    public Account() {
        this.transactionHistory = new Transaction[MAX_TRANSACTIONS];
    }
    /** 
    * Creates an account with an account name and balance
    * 
    * @param accountName name of the account
    * @param balance balance of the account
    */
    public Account(String accountName, double balance){
        
        //Rule 6 - MET00-J - Devin Diaz
        //Validate method arguments before using them
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be null or empty.");
        }

        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountName = accountName;
        this.balance = balance;
        this.transactionHistory = new Transaction[MAX_TRANSACTIONS];
    }
    /** 
    * Returns the account name
    * 
    * @return the account name
    */
    public String getAccountName() {
        return accountName;
    }
    /** 
    * Returns the balance of an account
    * 
    * @return balance of the account
    */
    public double getBalance() {
        synchronized(lock) {
            return balance;
        }
    }
    /** 
    * Sets the balance of an account
    * 
    * @param balance the new balance for the account
    */
    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        
        synchronized(lock) {
            this.balance = balance;
        }
    }
    /** 
    * Returns transaction history as an array
    * 
    * @return array of past transactions
    */
    public Transaction[] getTransactionHistory() {
        return transactionHistory;
    }
    /** 
    * Sets transation history to a new array
    * 
    * @param transactionHistory the new transaction history as an array
    */
    public void setTransactionHistory(Transaction[] transactionHistory) {
        this.transactionHistory = transactionHistory;
    }
    /** 
    * Creates and adds a new transaction to the transaction history and returns if successful
    * 
    * @param amount the amount of money in the transaction
    * @param type the type of transaction
    * @return if the transaction was successful
    */
    public boolean recordTransaction(double amount, Transaction.TransactionType type) {
        synchronized(lock) {
            Transaction incoming = new Transaction(amount, type, LocalDateTime.now(), balance);

            if (isDuplicate(incoming)) {
                System.out.println("Duplicate transaction detected. Action rejected.");
                return false;
            }

            addTransaction(incoming);
            return true;
        }
    }
    /** 
    * Determines whether a transaction is already in the transaction history
    * 
    * @param incoming the incoming transaction
    * @return if the transaction is already in the transaction history
    */
    private boolean isDuplicate(Transaction incoming) {
        Object[] incomingSignature = incoming.toSignature();
        for (Transaction existing : transactionHistory) {
            if (existing == null) 
                continue;
            Object[] existingSignature = existing.toSignature();
            if (Arrays.equals(incomingSignature, existingSignature)) {
                return true;
            }
        }
        return false;
    }
    /** 
    * Adds the transaction to the transaction history
    * 
    * @param transaction the transaction to be added
    */
    private void addTransaction(Transaction transaction) {
        for (int i = 0; i < transactionHistory.length; i++) {
            if (transactionHistory[i] == null) {
                transactionHistory[i] = transaction;
                return;
            }
        }
        System.out.println("Transaction history is full.");
    }
    /** 
    * Prints total transaction history
    * 
    */
    public void printHistory() {
        System.out.println("\n--- Transaction History: " + accountName + " ---");
        boolean hasTransactions = false;
        for (Transaction transaction : transactionHistory) {
            if (transaction != null) {
                System.out.println(transaction.getType() + " | $" + transaction.getAmount()
                    + " | Balance after: $" + transaction.getBalanceAfter()
                    + " | " + transaction.getTimestamp());
                hasTransactions = true;
            }
        }
        if (!hasTransactions) {
            System.out.println("No transactions on record.");
        }
    }

    // MET06: Do not invoke overridable methods in clone()
    @Override
    /** 
    Creates shallow clone of an account

    @return A shallow copy of account
    @throws CloneNotSupportedException If the object could not be cloned
    */
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }
}
