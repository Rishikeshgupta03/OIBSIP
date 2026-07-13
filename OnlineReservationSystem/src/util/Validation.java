package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Validation
 * ----------
 * Central place for all input-validation rules used across the
 * Online Reservation System. Keeping validation logic here avoids
 * duplicating regex/range checks inside multiple UI classes.
 *
 * Every method returns either a boolean or throws an
 * {@link IllegalArgumentException} with a user-friendly message,
 * which the UI layer catches and shows in a dialog.
 */
public final class Validation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z ]{1,49}$");
    private static final Pattern TRAIN_NUMBER_PATTERN = Pattern.compile("^\\d{5}$");

    private Validation() {
    }

    /**
     * Validates that a passenger name is non-empty and contains only
     * alphabets and spaces (2 to 50 characters).
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates that age is a whole number between 1 and 120 (inclusive).
     */
    public static boolean isValidAge(int age) {
        return age >= 1 && age <= 120;
    }

    /**
     * Parses a string into an age integer, throwing a friendly exception
     * on non-numeric input instead of letting NumberFormatException escape.
     */
    public static int parseAge(String ageText) {
        if (ageText == null || ageText.trim().isEmpty()) {
            throw new IllegalArgumentException("Age cannot be empty.");
        }
        try {
            int age = Integer.parseInt(ageText.trim());
            if (!isValidAge(age)) {
                throw new IllegalArgumentException("Age must be between 1 and 120.");
            }
            return age;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Age must be a valid whole number.");
        }
    }

    /**
     * Validates train number format: exactly 5 digits (matches sample data).
     */
    public static boolean isValidTrainNumberFormat(String trainNumber) {
        return trainNumber != null && TRAIN_NUMBER_PATTERN.matcher(trainNumber.trim()).matches();
    }

    /**
     * Validates that a field is not null/empty after trimming.
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Parses and validates a journey date in yyyy-MM-dd format,
     * ensuring it is not in the past (today is allowed).
     *
     * @throws IllegalArgumentException on bad format or a past date
     */
    public static LocalDate parseAndValidateJourneyDate(String dateText) {
        if (!isNotEmpty(dateText)) {
            throw new IllegalArgumentException("Journey date cannot be empty.");
        }
        try {
            LocalDate date = LocalDate.parse(dateText.trim());
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Journey date cannot be in the past.");
            }
            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Journey date must be in yyyy-MM-dd format.");
        }
    }

    /**
     * Validates that source and destination are both non-empty and different.
     */
    public static void validateSourceDestination(String source, String destination) {
        if (!isNotEmpty(source) || !isNotEmpty(destination)) {
            throw new IllegalArgumentException("Source and Destination cannot be empty.");
        }
        if (source.trim().equalsIgnoreCase(destination.trim())) {
            throw new IllegalArgumentException("Source and Destination cannot be the same.");
        }
    }
}
