import java.time.LocalDateTime;
/*
* OBJ10-J "Don’t use public static nonfinal variables" is followed in this file, no public static nonfinal variables are present
* MET01-J "Don’t use assertions in the final code" is followed in this file, no assertations are present
*/
public class Transaction {

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }

    private double amount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private double balanceAfter;
    /** 
    * Default constructor
    * <p>
    * Leaves fields uninitialized
    */
    public Transaction() {} // Required for Jackson

    /** 
    * Constructor for transaction
    * 
    * @param amount amount of money in the transaction
    * @param type type of transaction
    * @param timestamp date of the transaction
    * @param balanceAfter balance after the transaction
    */
    public Transaction(double amount, TransactionType type, LocalDateTime timestamp, double balanceAfter) {
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.balanceAfter = balanceAfter;
    }
    /** 
    * Gets the amount of money in the transaction
    * 
    * @return the amount of money in the transaction
    */
    public double getAmount(){ 
        return amount; 
    }
    /** 
    * Gets the type of the transaction
    * 
    * @return the type of the transcation
    */
    public TransactionType getType(){ 
        return type; 
    }
    /** 
    * Gets the date of the transaction
    * 
    * @return the date of the transaction
    */
    public LocalDateTime getTimestamp(){
        return timestamp; 

    }
    /** 
    * Gets the balance of the transaction
    * 
    * @return the balance of the transaction
    */
    public double getBalanceAfter(){
        return balanceAfter; 

    }
    
    public Object[] toSignature() {
        return new Object[]{ amount, type };
    }
}
