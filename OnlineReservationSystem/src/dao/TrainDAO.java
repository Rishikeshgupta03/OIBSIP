package dao;

import database.DBConnection;
import model.Train;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TrainDAO
 * --------
 * Data Access Object for the Trains table. Provides lookups used to
 * auto-fill the train name once a user types a train number, and to
 * validate that a train number actually exists before booking.
 */
public class TrainDAO {

    /**
     * Finds a train by its train number.
     *
     * @return the matching Train, or null if no train has that number
     */
    public Train findByTrainNumber(String trainNumber) throws SQLException {
        String sql = "SELECT train_number, train_name FROM Trains WHERE train_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trainNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Train(rs.getString("train_number"), rs.getString("train_name"));
                }
                return null;
            }
        }
    }

    /**
     * Returns every train currently stored in the database,
     * ordered by train number.
     */
    public List<Train> findAll() throws SQLException {
        String sql = "SELECT train_number, train_name FROM Trains ORDER BY train_number";
        List<Train> trains = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                trains.add(new Train(rs.getString("train_number"), rs.getString("train_name")));
            }
        }
        return trains;
    }
}
