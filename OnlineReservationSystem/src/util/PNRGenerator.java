package util;

import java.security.SecureRandom;

/**
 * PNRGenerator
 * ------------
 * Generates unique 10-digit numeric PNR (Passenger Name Record) codes
 * for new reservations.
 *
 * The actual uniqueness-against-the-database check happens in
 * {@code dao.ReservationDAO}, which will keep calling
 * {@link #generate()} in the rare case of a collision.
 */
public final class PNRGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PNRGenerator() {
    }

    /**
     * Generates a random 10-digit numeric PNR as a String.
     * The first digit is guaranteed to be non-zero so the PNR is
     * always exactly 10 characters long.
     */
    public static String generate() {
        StringBuilder pnr = new StringBuilder(10);
        pnr.append(1 + RANDOM.nextInt(9)); // first digit 1-9
        for (int i = 0; i < 9; i++) {
            pnr.append(RANDOM.nextInt(10)); // remaining digits 0-9
        }
        return pnr.toString();
    }
}
