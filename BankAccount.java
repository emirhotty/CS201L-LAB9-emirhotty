
import java.io.PrintWriter;

// Abstract superclass for all bank accounts
public abstract class BankAccount {
    protected int accountNumber;
    protected String accountType;
    protected String firstName;
    protected String lastName;
    protected String transactionDate;
    protected int transactionAmount;
    protected double accountBalance;
    protected double interestEarned;

    // Default constructor
    public BankAccount() {
        accountNumber = 0;
        accountType = "";
        firstName = "";
        lastName = "";
        transactionDate = "";
        transactionAmount = 0;
        accountBalance = 0;
        interestEarned = 0;
    }

    // Overloaded constructor
    public BankAccount(int accountNumber, String accountType,
                       String firstName, String lastName,
                       String transactionDate, int transactionAmount,
                       double accountBalance, double interestEarned) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.accountBalance = accountBalance;
        this.interestEarned = interestEarned;
    }

    // Getters
    public int getAccountNumber() { return accountNumber; }
    public String getAccountType() { return accountType; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getTransactionDate() { return transactionDate; }
    public int getTransactionAmount() { return transactionAmount; }
    public double getAccountBalance() { return accountBalance; }
    public double getInterestEarned() { return interestEarned; }

    // Setters
    public void setAccountNumber(int number) { accountNumber = number; }
    public void setAccountType(String type) { accountType = type; }
    public void setFirstName(String name) { firstName = name; }
    public void setLastName(String name) { lastName = name; }
    public void setTransactionDate(String date) { transactionDate = date; }
    public void setTransactionAmount(int amount) { transactionAmount = amount; }
    public void setAccountBalance(double balance) { accountBalance = balance; }
    public void setInterestEarned(double interest) { interestEarned = interest; }

    // PRE: Accepts a PrintWriter
    // POST: Prints account information to the report
    public void printDetails(PrintWriter out) {
        out.printf("%-20d%-20s%-20s%-15s%10.2f%15.2f%n",
                accountNumber, accountType, firstName, lastName,
                accountBalance, interestEarned);
    }

    // PRE: Accepts a valid deposit amount
    // POST: Adds the amount to the account balance
    public void processDeposit(int amt) {
        if (amt >= 0) {
            accountBalance += amt;
            transactionAmount = amt;
        }
    }

    // PRE: Accepts a valid withdrawal amount
    // POST: Subtracts the amount if enough money exists
    public void processWithdrawal(int amt) {
        if (amt >= 0 && accountBalance >= amt) {
            accountBalance -= amt;
            transactionAmount = amt;
        }
    }

    // PRE: Account has a balance
    // POST: Each subclass calculates its own interest
    public abstract void processInterestEarned();
}
