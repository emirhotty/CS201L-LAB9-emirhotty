
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String inputFile = "BankInfo.csv";
        String reportFile = "report.txt";
        String errorFile = "error.txt";

        ArrayList<BankAccount> accounts = new ArrayList<>();

        Functions.processFile(inputFile, errorFile, accounts);
        Functions.printReport(accounts, reportFile);

        System.out.println("Report generated in '" + reportFile + "'.");
        System.out.println("Errors (if any) logged in '" + errorFile + "'.");
    }
}
