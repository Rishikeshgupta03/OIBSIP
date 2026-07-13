package dao;

import database.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO
 * -------
 * Data Access Object for the Users table. Encapsulates every SQL
 * statement related to user authentication behind a small, clean
 * public API — UI code never writes SQL directly.
 *
 * Uses PreparedStatement exclusively; no SQL string concatenation.
 */
public class UserDAO {

    /**
     * Attempts to authenticate a user with the given username and password.
     *
     * @return the matching User if credentials are valid, otherwise null
     * @throws SQLException if a database access error occurs
     */
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
                return null;
            }
        }
    }

    /**
     * Checks whether a given username already exists in the Users table.
     */
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM Users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
