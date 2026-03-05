import java.io.IOException;
import java.util.Scanner;
/*
<<<<<<< HEAD
* OBJ10-J "Don’t use public static nonfinal variables" is followed in this file, no public static nonfinal variables are present
* MET01-J "Don’t use assertions in the final code" is followed in this file, no assertations are present
* MET12-J: Do not use finalizers
=======
* OBJ10-J "Don't use public static nonfinal variables" is followed in this file, no public static nonfinal variables are present
* MET01-J "Don't use assertions in the final code" is followed in this file, no assertations are present
>>>>>>> origin/main
*/
public class banking_main {

    private static int menuSelection = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        LoginService auth = new LoginService();
        CreateAccount createAccount = new CreateAccount();
        BalanceActions balanceActions = new BalanceActions();
        BankingAccountCreation bankingAccountCreation = new BankingAccountCreation();

        // Charles - initialize session thread pool and shutdown handler
        // TPS04-J: thread pool manages user sessions with proper ThreadLocal cleanup
        // FIO14-J: shutdown handler ensures cleanup at program termination
        SessionThreadPool sessionPool = new SessionThreadPool(3);
        BankingShutdownHandler shutdownHandler = new BankingShutdownHandler(sessionPool);

        // Charles - EXP00-J: checking the return value of directory initialization
        TransactionFileManager fileManager = new TransactionFileManager();
        boolean logDirReady = fileManager.initializeLogDirectory();
        if (!logDirReady) {
            System.out.println("Warning: transaction log directory could not be created.");
        }

        // Charles - TSM03-J: publisher for safely sharing account objects across threads
        AccountPublisher accountPublisher = new AccountPublisher();

        boolean userLoggedIn = false;
        UserInfo currentUser = null;

        while (true) {

            if (!userLoggedIn)
            {

                System.out.println("\nWelcome to 355 Banking app");
                System.out.println("1. Login");
                System.out.println("2. Forgot Password");
                System.out.println("3. Create Account");
                System.out.println("0. Quit");

                System.out.print("Entry: ");
                menuSelection = scanner.nextInt();
                scanner.nextLine();

                switch (menuSelection) {

                    case 0:
                        scanner.close();
                        // FIO14-J: shutdown handler will take care of cleanup automatically
                        return;

                    case 1:
                        currentUser = auth.handleLogin(scanner);

                        if (currentUser != null) {
                            userLoggedIn = true;
                        }

                        if (LoginService.forgotPassword()) {
                            System.out.println("forgot password call");
                            LoginService.forgotPassword(); // this can be handled in a seperate file
                        }
                        break;

                    case 2:
                        System.out.println("forgot password call");
                        LoginService.forgotPassword();
                        break;

                    case 3:
                        System.out.println("\n\n---- Creating Account ----\n\n");
                        UserInfo newUser = createAccount.create();
                        if (newUser != null){
                            userLoggedIn = true;
                            currentUser = newUser;
                        }
                        else
                            menuSelection = 1; // will route to login - false because account is already created
                        break;
                }
            }

            else {

                System.out.println("\n\nWelcome " + currentUser.getFirstName() + " " + currentUser.getLastName());

                System.out.println("--- Account Menu ---");
                System.out.println("1. Balance & Transactions");
                System.out.println("2. Create New Account");
                System.out.println("3. View Loans");
                System.out.println("4. Transaction History");
                System.out.println("5. Log out");


                menuSelection = scanner.nextInt();
                scanner.nextLine();

                switch (menuSelection) {

                    case 1:
                        //view balance actions
                        balanceActions.showBalanceMenu(currentUser);
                        break;

                    case 2:
                        //create new bank account
                        bankingAccountCreation.createNewAccount(currentUser);
                        // Charles - TSM01-J: SafeAccountBuilder doesnt let this escape during construction
                        SafeAccountBuilder builder = new SafeAccountBuilder(
                            "NewAccount", 0.0, "Checking");
                        System.out.println("Account built safely: " + builder.getAccountName());
                        break;

                    case 3:
                        System.out.println("loan(s) menu");
                        break;

                    case 4:
                        // Charles - EXP00-J: checking return value of export
                        boolean exported = fileManager.exportTransactionLog(
                            "history.txt", "Transaction history export");
                        if (!exported) {
                            System.out.println("Could not export transaction history.");
                        }
                        break;

                    case 5:
                        userLoggedIn = false;
                        System.out.println("Logged out.");
                        break;
                }
            }
        }
    }
}
