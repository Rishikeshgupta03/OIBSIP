package ui;

import dao.ReservationDAO;
import model.Reservation;
import util.DateUtil;
import util.Validation;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * ViewReservationFrame
 * ---------------------
 * The "View Reservation" screen: lets the user search for a booking
 * by PNR and displays the full details, or an informative message if
 * the PNR does not exist.
 */
public class ViewReservationFrame extends JFrame {

    private final DashboardFrame dashboardFrame;
    private final ReservationDAO reservationDAO = new ReservationDAO();

    private JTextField pnrField;
    private JTextArea detailsArea;

    public ViewReservationFrame(DashboardFrame dashboardFrame) {
        super("Online Reservation System - View Reservation");
        this.dashboardFrame = dashboardFrame;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(buildPanel());
    }

    private JPanel buildPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("View Reservation", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        searchPanel.add(new JLabel("Enter PNR:"));
        pnrField = new JTextField(14);
        searchPanel.add(pnrField);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        detailsArea = new JTextArea(14, 30);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(detailsArea);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> handleSearch());
        pnrField.addActionListener(e -> handleSearch()); // Enter key triggers search
        backButton.addActionListener(e -> {
            dispose();
            dashboardFrame.setVisible(true);
        });

        return mainPanel;
    }

    private void handleSearch() {
        String pnr = pnrField.getText().trim();

        if (!Validation.isNotEmpty(pnr)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a PNR to search.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Reservation reservation = reservationDAO.findByPnr(pnr);
            if (reservation == null) {
                detailsArea.setText("");
                JOptionPane.showMessageDialog(this,
                        "Reservation Not Found for PNR: " + pnr,
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            detailsArea.setText(formatDetails(reservation));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error while searching: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDetails(Reservation r) {
        StringBuilder sb = new StringBuilder();
        sb.append("PNR              : ").append(r.getPnr()).append("\n");
        sb.append("Passenger Name   : ").append(r.getPassengerName()).append("\n");
        sb.append("Age              : ").append(r.getAge()).append("\n");
        sb.append("Gender           : ").append(r.getGender()).append("\n");
        sb.append("Train Number     : ").append(r.getTrainNumber()).append("\n");
        sb.append("Train Name       : ").append(r.getTrainName()).append("\n");
        sb.append("Journey Date     : ").append(DateUtil.toDisplayFormat(r.getJourneyDate())).append("\n");
        sb.append("Class            : ").append(r.getTravelClass()).append("\n");
        sb.append("Source           : ").append(r.getSource()).append("\n");
        sb.append("Destination      : ").append(r.getDestination()).append("\n");
        sb.append("Booking Date     : ").append(DateUtil.toDisplayFormat(r.getBookingDate())).append("\n");
        return sb.toString();
    }
}
