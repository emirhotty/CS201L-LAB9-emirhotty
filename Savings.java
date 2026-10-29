
public class Savings extends BankAccount {

    // Overloaded constructor
    public Savings(int accountNumber, String accountType,
                   String firstName, String lastName,
                   String transactionDate, int transactionAmount,
                   double accountBalance, double interestEarned) {
        super(accountNumber, accountType, firstName, lastName,
              transactionDate, transactionAmount,
              accountBalance, interestEarned);
    }

    // PRE: Savings account has a balance
    // POST: Calculates and adds five days of interest
    @Override
    public void processInterestEarned() {
        double rate = 0;

        if (accountBalance >= 100 && accountBalance < 1000) {
            rate = 0.05;
        } else if (accountBalance >= 1000) {
            rate = 0.075;
        }

        double interest = accountBalance * rate * 5.0 / 365.0;

        interestEarned += interest;
        accountBalance += interest;
    }
}
