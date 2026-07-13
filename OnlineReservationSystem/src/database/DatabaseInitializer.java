package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseInitializer
 * --------------------
 * Responsible for creating the required SQLite tables (Users, Trains,
 * Reservations) if they do not already exist, and seeding them with
 * sample data (a default login user + 5 sample trains) so the
 * application is immediately usable after a fresh checkout.
 *
 * This class is intentionally idempotent: running it multiple times
 * (e.g. on every application startup) never duplicates data or fails,
 * thanks to "IF NOT EXISTS" DDL and existence checks before inserts.
 */
public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    /**
     * Creates all required tables (if missing) and seeds sample
     * users/trains (if missing). Should be called once at application
     * startup, before any DAO is used.
     *
     * @throws SQLException if any DDL/DML statement fails
     */
    public static void initialize() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Trains (
                        train_number TEXT PRIMARY KEY,
                        train_name TEXT NOT NULL
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Reservations (
                        pnr TEXT PRIMARY KEY,
                        passenger_name TEXT NOT NULL,
                        age INTEGER NOT NULL,
                        gender TEXT NOT NULL,
                        train_number TEXT NOT NULL,
                        train_name TEXT NOT NULL,
                        journey_date TEXT NOT NULL,
                        travel_class TEXT NOT NULL,
                        source TEXT NOT NULL,
                        destination TEXT NOT NULL,
                        booking_date TEXT NOT NULL,
                        FOREIGN KEY (train_number) REFERENCES Trains(train_number)
                    );
                    """);

            seedDefaultUser(conn);
            seedSampleTrains(conn);
        }
    }

    /**
     * Inserts a default login user (admin/admin123) only if the
     * Users table is currently empty.
     */
    private static void seedDefaultUser(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Users";
        try (Statement checkStmt = conn.createStatement()) {
            var rs = checkStmt.executeQuery(checkSql);
            if (rs.next() && rs.getInt(1) > 0) {
                return; // already has at least one user
            }
        }

        String insertSql = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, "admin");
            ps.setString(2, "admin123");
            ps.executeUpdate();
        }
    }

    /**
     * Inserts the 5 required sample trains, skipping any that already exist
     * (using "INSERT OR IGNORE" so re-running this method is always safe).
     */
    private static void seedSampleTrains(Connection conn) throws SQLException {
        String[][] sampleTrains = {
                {"12301", "Rajdhani Express"},
                {"12295", "Sanghamitra Express"},
                {"12555", "Gorakhdham Express"},
                {"22406", "Anand Vihar Express"},
                {"12951", "Mumbai Rajdhani"}
        };

        String insertSql = "INSERT OR IGNORE INTO Trains (train_number, train_name) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            for (String[] train : sampleTrains) {
                ps.setString(1, train[0]);
                ps.setString(2, train[1]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
