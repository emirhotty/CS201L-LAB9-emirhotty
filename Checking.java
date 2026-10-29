
public class Checking extends BankAccount {

    // Overloaded constructor
    public Checking(int accountNumber, String accountType,
                    String firstName, String lastName,
                    String transactionDate, int transactionAmount,
                    double accountBalance, double interestEarned) {
        super(accountNumber, accountType, firstName, lastName,
              transactionDate, transactionAmount,
              accountBalance, interestEarned);
    }

    // PRE: Checking account exists
    // POST: No interest is added to the account
    @Override
    public void processInterestEarned() {
        // Checking accounts do not earn interest
    }
}
