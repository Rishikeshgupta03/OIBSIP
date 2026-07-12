import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Account
 * -------
 * Represents a single customer bank account.
 *
 * Demonstrates:
 *  - Encapsulation (private fields with getters/setters)
 *  - Object composition (an Account "has-a" list of Transaction objects)
 *  - Business-logic methods (deposit/withdraw) kept inside the model,
 *    separate from the console UI logic which lives in ATM.java
 */
public class Account {

    private final String userId;
    private String pin;
    private final String accountNumber;
    private final String customerName;
    private double balance;
    private final String accountType;
    private final List<Transaction> transactionHistory;

    public Account(String userId, String pin, String accountNumber, String customerName,
                    double balance, String accountType) {
        this.userId = userId;
        this.pin = pin;
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
        this.accountType = accountType;
        this.transactionHistory = new ArrayList<>();
    }

    // ---------- Getters ----------

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    /** Returns an unmodifiable view of the transaction history (encapsulation). */
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    // ---------- Setters ----------

    public void setPin(String pin) {
        this.pin = pin;
    }

    // Balance is intentionally NOT given a public setter.
    // It may only change via deposit()/withdraw()/applyCredit()/applyDebit()
    // so that every balance change is always logged as a Transaction.

    // ---------- Business logic ----------

    /**
     * Verifies whether the supplied PIN matches this account's PIN.
     */
    public boolean isPinCorrect(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    /**
     * Adds funds to the account and records a transaction.
     */
    public void deposit(double amount, String description) {
        this.balance += amount;
        recordTransaction("DEPOSIT", amount, description);
    }

    /**
     * Removes funds from the account and records a transaction.
     * Caller is responsible for validating sufficient balance beforehand.
     */
    public void withdraw(double amount, String description) {
        this.balance -= amount;
        recordTransaction("WITHDRAW", amount, description);
    }

    /** Used internally by Bank when this account is the SENDER in a transfer. */
    public void applyDebit(double amount, String description) {
        this.balance -= amount;
        recordTransaction("TRANSFER-OUT", amount, description);
    }

    /** Used internally by Bank when this account is the RECEIVER in a transfer. */
    public void applyCredit(double amount, String description) {
        this.balance += amount;
        recordTransaction("TRANSFER-IN", amount, description);
    }

    private void recordTransaction(String type, double amount, String description) {
        Transaction txn = new Transaction(
                Utils.generateTransactionId(), type, amount, this.balance, description);
        this.transactionHistory.add(txn);
    }

    /**
     * Convenience method used only during application startup to seed
     * demo/sample transactions without them being labeled with today's
     * "real" balance-changing side effects beyond simple bookkeeping.
     */
    public void seedTransaction(String type, double amount, String description) {
        recordTransaction(type, amount, description);
    }
}
