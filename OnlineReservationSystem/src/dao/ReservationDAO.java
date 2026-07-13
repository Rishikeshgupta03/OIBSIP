package dao;

import database.DBConnection;
import model.Reservation;
import util.PNRGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ReservationDAO
 * --------------
 * Data Access Object for the Reservations table. Encapsulates every
 * SQL statement related to booking, retrieving, and cancelling
 * reservations. Uses PreparedStatement exclusively — never string
 * concatenation — to prevent SQL injection.
 */
public class ReservationDAO {

    /**
     * Checks whether a given PNR already exists in the Reservations table.
     */
    public boolean pnrExists(String pnr) throws SQLException {
        String sql = "SELECT 1 FROM Reservations WHERE pnr = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pnr);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Generates a guaranteed-unique 10-digit PNR by repeatedly calling
     * {@link PNRGenerator#generate()} until one is found that does not
     * already exist in the database. In practice this loop runs once,
     * since collisions among 10-digit codes are extremely rare.
     */
    public String generateUniquePnr() throws SQLException {
        String pnr;
        do {
            pnr = PNRGenerator.generate();
        } while (pnrExists(pnr));
        return pnr;
    }

    /**
     * Persists a new reservation to the database.
     *
     * @throws SQLException if the insert fails (e.g. duplicate PNR,
     *                       invalid train number foreign key)
     */
    public void save(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservations "
                + "(pnr, passenger_name, age, gender, train_number, train_name, "
                + "journey_date, travel_class, source, destination, booking_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservation.getPnr());
            ps.setString(2, reservation.getPassengerName());
            ps.setInt(3, reservation.getAge());
            ps.setString(4, reservation.getGender());
            ps.setString(5, reservation.getTrainNumber());
            ps.setString(6, reservation.getTrainName());
            ps.setString(7, reservation.getJourneyDate());
            ps.setString(8, reservation.getTravelClass());
            ps.setString(9, reservation.getSource());
            ps.setString(10, reservation.getDestination());
            ps.setString(11, reservation.getBookingDate());

            ps.executeUpdate();
        }
    }

    /**
     * Finds a reservation by its PNR.
     *
     * @return the matching Reservation, or null if no such PNR exists
     */
    public Reservation findByPnr(String pnr) throws SQLException {
        String sql = "SELECT * FROM Reservations WHERE pnr = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pnr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    /**
     * Deletes a reservation by PNR.
     *
     * @return true if a row was deleted, false if no matching PNR was found
     */
    public boolean deleteByPnr(String pnr) throws SQLException {
        String sql = "DELETE FROM Reservations WHERE pnr = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pnr);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getString("pnr"),
                rs.getString("passenger_name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("train_number"),
                rs.getString("train_name"),
                rs.getString("journey_date"),
                rs.getString("travel_class"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getString("booking_date")
        );
    }
}
