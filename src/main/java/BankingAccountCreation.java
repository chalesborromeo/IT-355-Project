import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/*
* OBJ10-J "Don't use public static nonfinal variables" is followed in this file, no public static nonfinal variables are present
* MET01-J "Don't use assertions in the final code" is followed in this file, no assertations are present
*/

/**
 * Handles the creation of new bank accounts (Checking or Savings)
 * for an existing logged-in user.
 */
public class BankingAccountCreation {
    //rule MET04, methods have appropriate access levels
    private final Scanner scanner;
    private final Random random;
    private final fileWriting fileOperations = new fileWriting();

    /**
     * Constructs a BankingAccountCreation instance and initializes the scanner and random generator.
     */
    public BankingAccountCreation() {
        //MET05, constructor only initializes fields rather than calling overridable methods
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    /**
     * Prompts the user to select an account type and initial deposit amount,
     * creates the account, adds it to the user's account list, and saves to file.
     *
     * @param user the currently logged-in user to create the account for
     */
    public void createNewAccount(UserInfo user) {
        //checks whether user is null. Rule EXP01
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("\n=== Create New Account ===");
        System.out.println("Select Account Type:");
        System.out.println("1. Checking");
        System.out.println("2. Savings");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        String accountType;

        //rule EXP03, uses .equals to compare instead of equality operators
        if ("1".equals(choice)) {
            accountType = "Checking";
        } else if ("2".equals(choice)) {
            accountType = "Savings";
        } else {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Enter initial deposit amount: ");
        String depositInput = scanner.nextLine();
        double initialDeposit;

        try {
            initialDeposit = Double.parseDouble(depositInput);
            if (initialDeposit < 0) {
                System.out.println("Deposit cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount.");
            return;
        }

        String generatedAccountName = accountType + "-" + generateAccountId();
        Account newAccount = new Account(generatedAccountName, initialDeposit);
        List<Account> accounts = user.getAccounts();

        //checks whether account is null before it is used. Rule EXP01
        if (accounts == null) {
            accounts = new ArrayList<>();
        }

        //VNA00,synchronization is used to make sure the updated value visible
        synchronized (accounts) {
            accounts.add(newAccount);
        }

        fileOperations.saveUser(user);
        System.out.println("Account successfully created!");
        System.out.println("Account Name: " + newAccount.getAccountName());
        System.out.println("Starting Balance: $" + newAccount.getBalance());
    }

    /**
     * Generates a random 6-digit account ID.
     *
     * @return a 6-digit numeric account ID as a String
     */
    private String generateAccountId() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
}