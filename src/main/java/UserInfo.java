import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    /*
	 * OBJ10-J (Don’t use public static nonfinal variables) is followed in this file, no public static nonfinal variables are present
	 * MET01-J (Don’t use assertions in the final code) is followed in this file, no assertations are present
	 */
    // Rule 4 Start - MET11 - Joey Pina
        // username, userId, and dob have been made final since these cannot be reassigned after it's constructed. 
        // Since that these are the key fields for the object userId - if they were reassignable then comparison would break
        // like when we are seeing if their file / account already exists. 
    private final String userId;
    private final String username;
    private String firstName;
    private String lastName;
    private String email;
    private final LocalDate dob;
    private String password; 
    private String ssn;
    private String pin;
    private List<Account> accounts;

    // Required for Jackson library
    /** 
    * Default constructor for UserInfo
    * <p>
    * sets all parameters to null
    */
    public UserInfo() {
        this.userId = null;
        this.username = null;
        this.dob = null;
    }

    // Rule 4 End - MET11 - Joey Pina
    /** 
    * Creates a UserInfo object
    * 
    * @param userId id of the user
    * @param username username of the user
    * @param firstName first name of the user
    * @param lastName last name of the user
    * @param email email of the user
    * @param dob date of birth of the user
    * @param password password for the user
    * @param ssn social security number of the user
    * @param pin pin of the user
    * @param accounts accounts held by the user
    */
    public UserInfo(String userId, String username, 
                    String firstName, String lastName,
                    String email, LocalDate dob,
                    String password, String ssn, 
                    String pin, List<Account> accounts) {

        this.userId      = userId;
        this.username    = username;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.dob         = dob;
        this.password    = password;
        this.ssn         = ssn;
        this.pin         = pin;
        this.accounts    = accounts;
    }
    /** 
    * Gets the user's id
    * 
    * @return the user's id
    */
    public String getUserId(){
        return userId;
    }
    /** 
    * Gets the user's username
    * 
    * @return the user's username
    */
    public String getUsername() {
        return username;
    }
    /** 
    * Gets the user's first name
    * 
    * @return the user's first name
    */
    public String getFirstName(){
        return firstName;
    }
    /** 
    * Gets the user's last name
    * 
    * @return the user's last name
    */
    public String getLastName(){
        return lastName;
    }
    /** 
    * Gets the user's email
    * 
    * @return the user's email
    */
    public String getEmail(){
        return email;
    }
    /** 
    * Gets the user's date of birth
    * 
    * @return the user's date of birth
    */
    public LocalDate getDob(){
        return dob;
    }
    /** 
    * Gets the user's password
    * 
    * @return the user's password
    */
    public String getPassword(){
        return password;
    }
    /** 
    * Gets the user's social security number
    * 
    * @return the user's social security number
    */
    public String getSsn(){
        return ssn;
    }
    /** 
    * Gets the user's pin
    * 
    * @return the user's pin
    */
    public String getPin(){
        return pin;
    }
    /** 
    * Gets the user's accounts
    * 
    * @return the user's accounts
    */
    public List<Account> getAccounts(){
        //Rule 5, OBJ05-J Returns a clone of the object instead of a reference
        //return new ArrayList<Account>(accounts);
        //Recommendation 6 - MET55-J - Devin Diaz
        //Return an empty collection instead of null
        // if (accounts == null) {
        //     return new java.util.ArrayList<>();
        // }
        return accounts;
    }
    /** 
    * Validates that a password is correct
    * 
    * @param inputPassword the input password
    * @return if the input password matches the user's password
    */
    public boolean validatePassword(String inputPassword){
        return password.equals(inputPassword);
    }
    /** 
    * Validates that a pin is correct
    * 
    * @param inputPin the input pin
    * @return if the input pin matches the user's pin
    */
    public boolean validatePin(String inputPin){
        return pin.equals(inputPin);
    }
}
