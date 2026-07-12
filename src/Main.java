import java.util.Scanner;

/**
 * Main
 * ----
 * Application entry point.
 *
 * Responsibilities:
 *  1. Create the Bank and seed it with predefined sample accounts.
 *  2. Seed a few sample transactions for demonstration purposes.
 *  3. Launch the ATM console UI.
 *  4. Wrap everything in a top-level try-catch so the application
 *     NEVER terminates unexpectedly due to an unhandled exception.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Bank bank = new Bank();
            seedSampleAccounts(bank);
            seedSampleTransactions(bank);

            ATM atm = new ATM(bank, scanner);
            atm.start();

        } catch (Exception e) {
            // Final safety net: catches ANY unexpected exception so the
            // console application never crashes with a raw stack trace.
            System.out.println();
            System.out.println("A critical error occurred: " + e.getMessage());
            System.out.println("The application will now close safely.");
        } finally {
            scanner.close();
        }
    }

    /**
     * Creates 5 predefined demo users/accounts with different
     * User IDs, PINs, balances, account numbers and account types.
     */
    private static void seedSampleAccounts(Bank bank) {
        bank.addAccount(new Account("john01", "1111", "1000000001", "John Carter", 15000.00, "Savings"));
        bank.addAccount(new Account("sara22", "2222", "1000000002", "Sara Williams", 32500.75, "Current"));
        bank.addAccount(new Account("amit99", "3333", "1000000003", "Amit Sharma", 8250.00, "Savings"));
        bank.addAccount(new Account("lisa07", "4444", "1000000004", "Lisa Brown", 120000.00, "Current"));
        bank.addAccount(new Account("raj45", "5555", "1000000005", "Raj Patel", 500.50, "Savings"));
    }

    /**
     * Seeds a few demonstration transactions into some accounts so that
     * "Transaction History" has meaningful sample data right from launch.
     */
    private static void seedSampleTransactions(Bank bank) {
        Account john = bank.findByUserId("john01");
        if (john != null) {
            john.seedTransaction("DEPOSIT", 5000.00, "Initial Cash Deposit");
            john.seedTransaction("WITHDRAW", 1200.00, "ATM Cash Withdrawal");
        }

        Account sara = bank.findByUserId("sara22");
        if (sara != null) {
            sara.seedTransaction("DEPOSIT", 10000.00, "Salary Credit");
        }

        Account amit = bank.findByUserId("amit99");
        if (amit != null) {
            amit.seedTransaction("WITHDRAW", 500.00, "Grocery Shopping Withdrawal");
        }
    }
}
