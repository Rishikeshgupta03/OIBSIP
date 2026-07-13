package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * DateUtil
 * --------
 * Centralizes date parsing/formatting so the rest of the application
 * (UI + DAO layers) always agrees on a single date format: ISO
 * yyyy-MM-dd, which is also what SQLite stores as TEXT and what
 * {@link LocalDate#parse(CharSequence)} accepts by default.
 */
public final class DateUtil {

    public static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private DateUtil() {
    }

    /** Returns today's date formatted as yyyy-MM-dd (ISO, used for DB storage). */
    public static String todayAsIso() {
        return LocalDate.now().toString();
    }

    /** Converts an ISO (yyyy-MM-dd) date string to a friendly display format. */
    public static String toDisplayFormat(String isoDate) {
        if (isoDate == null || isoDate.isBlank()) {
            return "";
        }
        try {
            LocalDate date = LocalDate.parse(isoDate.trim());
            return date.format(DISPLAY_FORMATTER);
        } catch (DateTimeParseException e) {
            return isoDate; // fall back to raw value rather than crashing
        }
    }

    /** Returns true if the given ISO date string represents today or a future date. */
    public static boolean isTodayOrFuture(String isoDate) {
        try {
            LocalDate date = LocalDate.parse(isoDate.trim());
            return !date.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
