import java.time.LocalDateTime;

/**
 * Transaction
 * -----------
 * Represents a single banking transaction (Deposit, Withdrawal,
 * Transfer-In, Transfer-Out, etc.) performed on an {@link Account}.
 *
 * This class follows the Encapsulation principle: all fields are private
 * and only exposed through getters, since a transaction record should be
 * immutable once created (a real bank never edits a past transaction).
 */
public class Transaction {

    private final String transactionId;
    private final LocalDateTime dateTime;
    private final String type;          // e.g. DEPOSIT, WITHDRAW, TRANSFER-IN, TRANSFER-OUT
    private final double amount;
    private final double availableBalance; // balance AFTER this transaction
    private final String description;

    public Transaction(String transactionId, LocalDateTime dateTime, String type,
                        double amount, double availableBalance, String description) {
        this.transactionId = transactionId;
        this.dateTime = dateTime;
        this.type = type;
        this.amount = amount;
        this.availableBalance = availableBalance;
        this.description = description;
    }

    // Overloaded constructor: automatically stamps the current date/time.
    public Transaction(String transactionId, String type, double amount,
                        double availableBalance, String description) {
        this(transactionId, LocalDateTime.now(), type, amount, availableBalance, description);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns a single formatted table row representing this transaction,
     * used when printing the transaction history.
     */
    public String toTableRow() {
        return String.format("%-15s %-22s %-14s %-15s %-15s %-20s",
                transactionId,
                Utils.formatDate(dateTime),
                type,
                Utils.formatCurrency(amount),
                Utils.formatCurrency(availableBalance),
                description);
    }

    @Override
    public String toString() {
        return toTableRow();
    }
}
