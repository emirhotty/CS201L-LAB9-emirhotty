
import java.io.*;
import java.util.*;

public class Functions {

    // PRE: Accepts a string
    // POST: Returns true if the string contains only digits
    public static boolean isDigits(String str) {
        return str != null && str.matches("\\d+");
    }

    // PRE: Accepts an account list and account number
    // POST: Returns true if the account exists
    public static boolean accountExists(ArrayList<BankAccount> accounts, int acct) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == acct) {
                return true;
            }
        }
        return false;
    }

    // PRE: Accepts an account list and account number
    // POST: Returns the matching account or null
    public static BankAccount findAccount(ArrayList<BankAccount> accounts, int acct) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == acct) {
                return account;
            }
        }
        return null;
    }

    // PRE: Accepts an account type
    // POST: Returns true if type is C, S, or CD
    public static boolean validType(String t) {
        return t.equalsIgnoreCase("C") ||
               t.equalsIgnoreCase("S") ||
               t.equalsIgnoreCase("CD");
    }

    // PRE: Accepts a transaction type
    // POST: Returns true if transaction type is valid
    public static boolean validTransType(String t) {
        if (t.equalsIgnoreCase("NEW") ||
            t.equalsIgnoreCase("DEPOSIT") ||
            t.equalsIgnoreCase("WITHDRAWAL") ||
            t.equalsIgnoreCase("TRANSFER") ||
            t.equalsIgnoreCase("INTEREST")) {
            return true;
        }
        return false;
    }

    // PRE: Accepts a date string
    // POST: Returns 1 if valid, -1 if invalid
    public static int validDate(String date) {
        try {
            String[] parts = date.split("/");

            if (parts.length != 3) {
                return -1;
            }

            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (year < 1900 || year > 2024) {
                return -1;
            }

            if (month < 1 || month > 12) {
                return -1;
            }

            int daysInMonth = 31;

            if (month == 4 || month == 6 ||
                month == 9 || month == 11) {
                daysInMonth = 30;
            }

            if (month == 2) {
                daysInMonth = 28;

                if (year % 400 == 0 ||
                    (year % 4 == 0 && year % 100 != 0)) {
                    daysInMonth = 29;
                }
            }

            if (day < 1 || day > daysInMonth) {
                return -1;
            }

            return 1;

        } catch (Exception e) {
            return -1;
        }
    }

    // PRE: Accepts input file, error file, and account list
    // POST: Processes transactions and records invalid entries
    public static void processFile(String filename, String errorLog,
                                   ArrayList<BankAccount> accounts) {

        try (
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            PrintWriter errWriter = new PrintWriter(new FileWriter(errorLog))
        ) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",", -1);

                try {
                    if (tokens.length != 7) {
                        errWriter.println("Parse error in record: " + line);
                        continue;
                    }

                    String acctString = tokens[0].trim();
                    String type = tokens[1].trim();
                    String firstName = tokens[2].trim();
                    String lastName = tokens[3].trim();
                    String date = tokens[4].trim();
                    String transType = tokens[5].trim().toUpperCase();
                    String amountString = tokens[6].trim();

                    // Check transaction type
                    if (!validTransType(transType)) {
                        errWriter.println("Transaction type invalid: " + line);
                        continue;
                    }

                    // Check date
                    if (validDate(date) == -1) {
                        errWriter.println("Date invalid: " + line);
                        continue;
                    }

                    // Apply interest to all accounts
                    if (transType.equals("INTEREST")) {
                        for (BankAccount account : accounts) {
                            account.processInterestEarned();
                        }
                        continue;
                    }

                    // Check account number
                    if (!isDigits(acctString)) {
                        errWriter.println("Account number invalid: " + line);
                        continue;
                    }

                    int acctNumber = Integer.parseInt(acctString);

                    // Check transaction amount
                    if (!isDigits(amountString)) {
                        errWriter.println("Transaction Amount invalid: " + line);
                        continue;
                    }

                    int amount = Integer.parseInt(amountString);

                    // Create new account
                    if (transType.equals("NEW")) {

                        if (!validType(type)) {
                            errWriter.println("Account type invalid: " + line);
                            continue;
                        }

                        if (accountExists(accounts, acctNumber)) {
                            errWriter.println("NEW: Account already exists: " + line);
                            continue;
                        }

                        BankAccount newAccount;

                        if (type.equalsIgnoreCase("C")) {
                            newAccount = new Checking(
                                acctNumber, "C", firstName, lastName,
                                date, amount, amount, 0);
                        } else if (type.equalsIgnoreCase("S")) {
                            newAccount = new Savings(
                                acctNumber, "S", firstName, lastName,
                                date, amount, amount, 0);
                        } else {
                            newAccount = new Certificate(
                                acctNumber, "CD", firstName, lastName,
                                date, amount, amount, 0);
                        }

                        accounts.add(newAccount);
                    }

                    // Process deposit
                    else if (transType.equals("DEPOSIT")) {

                        if (!validType(type)) {
                            errWriter.println("Account type invalid: " + line);
                            continue;
                        }

                        BankAccount account = findAccount(accounts, acctNumber);

                        if (account == null) {
                            errWriter.println("DEPOSIT: Account number does not exist: " + line);
                            continue;
                        }

                        if (!account.getAccountType().equalsIgnoreCase(type)) {
                            errWriter.println("DEPOSIT: Account type invalid: " + line);
                            continue;
                        }

                        account.processDeposit(amount);
                        account.setTransactionDate(date);
                    }

                    // Process withdrawal
                    else if (transType.equals("WITHDRAWAL")) {

                        BankAccount account = findAccount(accounts, acctNumber);

                        if (account == null) {
                            errWriter.println("WITHDRAWAL: Account number does not exist: " + line);
                            continue;
                        }

                        if (account.getAccountBalance() < amount) {
                            errWriter.println("WITHDRAWAL: Account balance is invalid: " + line);
                            continue;
                        }

                        account.processWithdrawal(amount);
                        account.setTransactionDate(date);
                    }

                    // Process transfer
                    else if (transType.equals("TRANSFER")) {

                        if (!isDigits(type)) {
                            errWriter.println("TRANSFER: One of the Account numbers does not exist: " + line);
                            continue;
                        }

                        int toNumber = Integer.parseInt(type);

                        BankAccount fromAccount = findAccount(accounts, acctNumber);
                        BankAccount toAccount = findAccount(accounts, toNumber);

                        if (fromAccount == null || toAccount == null) {
                            errWriter.println("TRANSFER: One of the Account numbers does not exist: " + line);
                            continue;
                        }

                        if (fromAccount.getAccountBalance() < amount) {
                            errWriter.println("TRANSFER: From Account balance is invalid: " + line);
                            continue;
                        }

                        if (fromAccount == toAccount) {
                            continue;
                        }

                        fromAccount.processWithdrawal(amount);
                        toAccount.processDeposit(amount);

                        fromAccount.setTransactionDate(date);
                        toAccount.setTransactionDate(date);
                    }

                } catch (NumberFormatException e) {
                    errWriter.println("Numeric value invalid: " + line);
                } catch (Exception e) {
                    errWriter.println("Parse error in record: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // PRE: Accepts account list and report filename
    // POST: Sorts accounts and prints report
    public static void printReport(ArrayList<BankAccount> accounts,
                                   String reportFile) {

        accounts.sort(Comparator.comparing(BankAccount::getAccountNumber));

        try (PrintWriter out = new PrintWriter(new FileWriter(reportFile))) {

            out.printf("%-20s%-20s%-20s%-15s%10s%15s%n",
                    "Account#", "Type", "First Name",
                    "Last Name", "Balance", "Interest");

            for (BankAccount acct : accounts) {
                acct.printDetails(out);
            }

        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}
