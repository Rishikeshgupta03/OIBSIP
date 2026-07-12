import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utils
 * -----
 * A collection of static utility/helper methods used across the ATM
 * application. Keeping these methods in one place avoids code duplication
 * and separates generic "helper" logic from business logic.
 *
 * Responsibilities:
 *  - Currency formatting
 *  - Date/time formatting
 *  - Console UI helpers (headers, separator lines)
 *  - Safe (crash-proof) console input reading for integers and doubles
 */
public final class Utils {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss a");

    // Private constructor -> this is a pure utility class, no instances needed.
    private Utils() {
    }

    /**
     * Formats a monetary amount to a currency-style string with exactly
     * two decimal places, e.g. 1250.5 -> "Rs. 1250.50"
     */
    public static String formatCurrency(double amount) {
        return String.format("Rs. %,.2f", amount);
    }

    /**
     * Formats a LocalDateTime into a human-readable string,
     * e.g. "12-Jul-2026 04:35:10 PM"
     */
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }

    /**
     * Prints a bold section header surrounded by a line of '=' characters.
     */
    public static void printHeader(String title) {
        String line = "=".repeat(60);
        System.out.println(line);
        int padding = Math.max(0, (60 - title.length()) / 2);
        System.out.println(" ".repeat(padding) + title);
        System.out.println(line);
    }

    /**
     * Prints a thin separator line, used to visually divide console sections.
     */
    public static void printLine() {
        System.out.println("-".repeat(60));
    }

    /**
     * Safely reads an integer from the console.
     * Keeps prompting until the user enters a syntactically valid integer,
     * never allowing the application to crash on bad input.
     *
     * @param scanner the Scanner reading from System.in
     * @param prompt  the message shown to the user
     * @return a valid integer entered by the user
     */
    public static int readSafeInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                if (input.isEmpty()) {
                    throw new NumberFormatException("Empty input");
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Safely reads a double (decimal) value from the console.
     * Keeps prompting until the user enters a syntactically valid number,
     * never allowing the application to crash on bad input.
     *
     * @param scanner the Scanner reading from System.in
     * @param prompt  the message shown to the user
     * @return a valid double entered by the user
     */
    public static double readSafeDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                if (input.isEmpty()) {
                    throw new NumberFormatException("Empty input");
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid amount (numbers only).");
            }
        }
    }

    /**
     * Safely reads a non-empty line of text from the console.
     */
    public static String readSafeString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    /**
     * Generates a pseudo-unique transaction ID using the current
     * nanoTime value. Simple and sufficient for this in-memory console app.
     */
    public static String generateTransactionId() {
        return "TXN" + (System.nanoTime() % 1_000_000_000L);
    }
}
