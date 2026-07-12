import java.util.List;
import java.util.Scanner;

/**
 * ATM
 * ---
 * Handles all console-based user interaction: login flow, main menu,
 * and each banking operation's UI (deposit, withdraw, transfer, etc.).
 *
 * This class deliberately contains NO core banking business rules itself
 * (those live in Account/Bank) - it only orchestrates user input/output
 * and delegates the actual work, keeping UI logic and business logic
 * cleanly separated.
 */
public class ATM {

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private final Bank bank;
    private final Scanner scanner;

    public ATM(Bank bank, Scanner scanner) {
        this.bank = bank;
        this.scanner = scanner;
    }

    /**
     * Starts the application: repeatedly shows the login screen.
     * After a successful login, shows the main menu until the user
     * logs out (returns here) or exits (terminates the whole app).
     */
    public void start() {
        Utils.printHeader("WELCOME TO THE ATM INTERFACE SYSTEM");
        boolean running = true;

        while (running) {
            Account loggedInAccount = login();
            if (loggedInAccount == null) {
                // Account was locked after too many failed attempts.
                running = false;
                break;
            }
            running = showMainMenu(loggedInAccount);
        }

        Utils.printLine();
        System.out.println("Thank you for using ATM Interface.");
        System.out.println("Goodbye!");
        Utils.printLine();
    }

    // ---------------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------------

    /**
     * Handles the login screen, allowing up to MAX_LOGIN_ATTEMPTS
     * incorrect attempts before locking the application.
     *
     * @return the authenticated Account, or null if login failed
     *         permanently (account locked / application should exit).
     */
    private Account login() {
        Utils.printHeader("LOGIN");
        int attemptsRemaining = MAX_LOGIN_ATTEMPTS;

        while (attemptsRemaining > 0) {
            String userId = Utils.readSafeString(scanner, "Enter User ID: ");
            String pin = Utils.readSafeString(scanner, "Enter PIN: ");

            Account account = bank.authenticate(userId, pin);
            if (account != null) {
                System.out.println();
                System.out.println("Login successful. Welcome, " + account.getCustomerName() + "!");
                Utils.printLine();
                return account;
            }

            attemptsRemaining--;
            if (attemptsRemaining > 0) {
                System.out.println("Invalid User ID or PIN. Attempts remaining: " + attemptsRemaining);
                Utils.printLine();
            }
        }

        System.out.println();
        Utils.printLine();
        System.out.println("Account Locked.");
        System.out.println("Exiting Application...");
        Utils.printLine();
        return null;
    }

    // ---------------------------------------------------------------
    // MAIN MENU
    // ---------------------------------------------------------------

    /**
     * Displays and drives the main menu loop for a logged-in account.
     *
     * @return true  -> user chose Logout (return to login screen, keep app running)
     *         false -> user chose Exit (terminate the whole application)
     */
    private boolean showMainMenu(Account account) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println();
            Utils.printHeader("MAIN MENU");
            System.out.println("1. Balance Inquiry");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Transaction History");
            System.out.println("6. Account Details");
            System.out.println("7. Logout");
            System.out.println("8. Exit");
            Utils.printLine();

            int choice = Utils.readSafeInt(scanner, "Choose an option (1-8): ");

            switch (choice) {
                case 1:
                    balanceInquiry(account);
                    break;
                case 2:
                    depositMoney(account);
                    break;
                case 3:
                    withdrawMoney(account);
                    break;
                case 4:
                    transferMoney(account);
                    break;
                case 5:
                    transactionHistory(account);
                    break;
                case 6:
                    accountDetails(account);
                    break;
                case 7:
                    System.out.println("Logging out... Returning to login screen.");
                    return true; // logout -> keep application running
                case 8:
                    return false; // exit -> terminate application
                default:
                    System.out.println("Invalid menu option. Please choose a number between 1 and 8.");
            }
        }
        return true;
    }

    // ---------------------------------------------------------------
    // MENU OPERATIONS
    // ---------------------------------------------------------------

    private void balanceInquiry(Account account) {
        Utils.printHeader("BALANCE INQUIRY");
        System.out.println("Current Balance: " + Utils.formatCurrency(account.getBalance()));
        Utils.printLine();
    }

    private void depositMoney(Account account) {
        Utils.printHeader("DEPOSIT MONEY");
        double amount = Utils.readSafeDouble(scanner, "Enter amount to deposit: ");

        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be greater than zero.");
            }
            account.deposit(amount, "Cash Deposit");
            System.out.println("Deposit successful!");
            System.out.println("New Balance: " + Utils.formatCurrency(account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
        Utils.printLine();
    }

    private void withdrawMoney(Account account) {
        Utils.printHeader("WITHDRAW MONEY");
        double amount = Utils.readSafeDouble(scanner, "Enter amount to withdraw: ");

        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
            }
            if (amount > account.getBalance()) {
                throw new IllegalArgumentException("Insufficient Balance");
            }
            account.withdraw(amount, "Cash Withdrawal");
            System.out.println("Withdrawal successful!");
            System.out.println("New Balance: " + Utils.formatCurrency(account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
        Utils.printLine();
    }

    private void transferMoney(Account account) {
        Utils.printHeader("TRANSFER MONEY");
        String receiverAccountNumber = Utils.readSafeString(scanner, "Enter destination Account Number: ");
        double amount = Utils.readSafeDouble(scanner, "Enter amount to transfer: ");

        try {
            bank.transfer(account, receiverAccountNumber, amount);
            System.out.println("Transfer successful!");
            System.out.println("New Balance: " + Utils.formatCurrency(account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        Utils.printLine();
    }

    private void transactionHistory(Account account) {
        Utils.printHeader("TRANSACTION HISTORY");
        List<Transaction> history = account.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println("No Transactions Available.");
        } else {
            System.out.printf("%-15s %-22s %-14s %-15s %-15s %-20s%n",
                    "Txn ID", "Date & Time", "Type", "Amount", "Balance", "Description");
            Utils.printLine();
            for (Transaction txn : history) {
                System.out.println(txn.toTableRow());
            }
        }
        Utils.printLine();
    }

    private void accountDetails(Account account) {
        Utils.printHeader("ACCOUNT DETAILS");
        System.out.println("Customer Name   : " + account.getCustomerName());
        System.out.println("User ID         : " + account.getUserId());
        System.out.println("Account Number  : " + account.getAccountNumber());
        System.out.println("Account Type    : " + account.getAccountType());
        System.out.println("Current Balance : " + Utils.formatCurrency(account.getBalance()));
        Utils.printLine();
    }
}
