
public class Certificate extends BankAccount {

    private boolean withdrawalTaken = false;

    // Overloaded constructor
    public Certificate(int accountNumber, String accountType,
                       String firstName, String lastName,
                       String transactionDate, int transactionAmount,
                       double accountBalance, double interestEarned) {
        super(accountNumber, accountType, firstName, lastName,
              transactionDate, transactionAmount,
              accountBalance, interestEarned);
    }

    // PRE: Accepts a withdrawal amount
    // POST: Withdraws money and records the withdrawal
    @Override
    public void processWithdrawal(int amt) {
        if (amt >= 0 && accountBalance >= amt) {
            super.processWithdrawal(amt);
            withdrawalTaken = true;
        }
    }

    // PRE: CD account has a balance
    // POST: Applies interest and resets withdrawal tracking
    @Override
    public void processInterestEarned() {
        double rate;

        if (withdrawalTaken) {
            rate = 0.025;
        } else {
            rate = 0.10;
        }

        double interest = accountBalance * rate * 5.0 / 365.0;

        interestEarned += interest;
        accountBalance += interest;

        withdrawalTaken = false;
    }
}
