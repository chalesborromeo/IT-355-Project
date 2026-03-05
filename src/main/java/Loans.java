import java.io.*;

public class Loans implements Serializable {

    private String loanId;
    private double amount;

    public Loans(String loanId, double amount) {
        this.loanId = loanId;
        this.amount = amount;
    }

    //Rule 14 - SER01-J - Devin Diaz
    //Correct readObject signature for custom deserialization logic.
    /** 
    * Reads a loan in from the ObjectInputStream
    * 
    * @param in 
    * @throws InvalidObjectException if the read loan id is null
    */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        if (loanId == null) {
            throw new InvalidObjectException("Loan ID cannot be null.");
        }
    }

    //Rule 14 - SER04-J - Devin Diaz
    //Check security permissions before serialization.
    /** 
    * Writes an object to a specified output stream
    * 
    * @param out the output stream to write to 
    * @throws IOException if problem with serialization
    */
    private void writeObject(ObjectOutputStream out) throws IOException {

        SecurityManager sm = System.getSecurityManager();

        if (sm != null) {
            sm.checkPermission(new SerializablePermission("enableSubstitution"));
        }

        out.defaultWriteObject();
    }

    //Rule 14 - SER05-J - Devin Diaz
    //Static nested classes are safe to serialize
    public static class LoanRecord implements Serializable {

        private String recordId;
        /** 
        * Constructor for making a loan record
        * 
        * @param id the record id
        */
        public LoanRecord(String id) {
            this.recordId = id;
        }
    }
}