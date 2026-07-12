import java.util.HashMap;
import java.util.Map;

/**
 * Bank
 * ----
 * Represents the bank itself: a collection of {@link Account} objects and
 * the operations that involve MORE THAN ONE account (like transfers) or
 * require looking accounts up (like authentication).
 *
 * Keeping this logic in its own class (rather than stuffing it into ATM.java)
 * follows the Single Responsibility Principle: Bank manages accounts/data,
 * while ATM.java is only responsible for the console UI/menu flow.
 */
public class Bank {

    // Key = userId, Value = Account. A HashMap gives fast O(1) login lookups.
    private final Map<String, Account> accountsByUserId;

    // Secondary index for fast lookup by account number (used for transfers).
    private final Map<String, Account> accountsByAccountNumber;

    public Bank() {
        this.accountsByUserId = new HashMap<>();
        this.accountsByAccountNumber = new HashMap<>();
    }

    /**
     * Registers a new account with the bank.
     */
    public void addAccount(Account account) {
        accountsByUserId.put(account.getUserId(), account);
        accountsByAccountNumber.put(account.getAccountNumber(), account);
    }

    /**
     * Looks up an account by its User ID. Returns null if not found.
     */
    public Account findByUserId(String userId) {
        return accountsByUserId.get(userId);
    }

    /**
     * Looks up an account by its Account Number. Returns null if not found.
     */
    public Account findByAccountNumber(String accountNumber) {
        return accountsByAccountNumber.get(accountNumber);
    }

    /**
     * Attempts to authenticate a user with the given User ID and PIN.
     *
     * @return the matching Account if credentials are valid, otherwise null.
     */
    public Account authenticate(String userId, String pin) {
        Account account = findByUserId(userId);
        if (account == null) {
            return null;
        }
        return account.isPinCorrect(pin) ? account : null;
    }

    /**
     * Transfers money from one account to another.
     * Performs all necessary validation and updates both accounts'
     * balances and transaction histories atomically (within this call).
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void transfer(Account sender, String receiverAccountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }
        if (sender.getAccountNumber().equals(receiverAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer money to your own account.");
        }
        Account receiver = findByAccountNumber(receiverAccountNumber);
        if (receiver == null) {
            throw new IllegalArgumentException("Destination account number does not exist.");
        }
        if (amount > sender.getBalance()) {
            throw new IllegalArgumentException("Insufficient balance for this transfer.");
        }

        sender.applyDebit(amount, "Transfer to " + receiver.getAccountNumber()
                + " (" + receiver.getCustomerName() + ")");
        receiver.applyCredit(amount, "Transfer from " + sender.getAccountNumber()
                + " (" + sender.getCustomerName() + ")");
    }

    public int getTotalAccounts() {
        return accountsByUserId.size();
    }
}
