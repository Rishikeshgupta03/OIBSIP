package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection
 * ------------
 * Responsible solely for opening JDBC connections to the SQLite
 * database file used by this application. Centralizing this here
 * means every DAO class shares the exact same connection logic and
 * database file path.
 *
 * The database file lives at "database/reservation.db" relative to
 * the directory the application is launched from, matching the
 * project's required folder structure.
 */
public final class DBConnection {

    private static final String DB_FOLDER = "database";
    private static final String DB_FILE_NAME = "reservation.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FOLDER + File.separator + DB_FILE_NAME;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "SQLite JDBC driver not found on classpath. "
                            + "Make sure sqlite-jdbc-*.jar is included when compiling/running.", e);
        }
    }

    private DBConnection() {
    }

    /**
     * Opens (and returns) a new JDBC connection to the SQLite database file.
     * Ensures the "database" folder exists before attempting to connect,
     * since SQLite will not auto-create missing parent directories.
     *
     * @return an open java.sql.Connection
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return DriverManager.getConnection(DB_URL);
    }
}
