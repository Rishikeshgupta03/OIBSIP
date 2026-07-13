package ui;

import dao.ReservationDAO;
import dao.TrainDAO;
import model.Reservation;
import model.Train;
import util.DateUtil;
import util.Validation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * ReservationFrame
 * ----------------
 * The "Reserve Ticket" screen: collects passenger and journey details,
 * validates every field, auto-fills the train name from the train
 * number, generates a unique PNR, and persists the booking through
 * {@link ReservationDAO}.
 */
public class ReservationFrame extends JFrame {

    private final DashboardFrame dashboardFrame;
    private final TrainDAO trainDAO = new TrainDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> genderCombo;
    private JTextField trainNumberField;
    private JTextField trainNameField;
    private JTextField journeyDateField;
    private JComboBox<String> classCombo;
    private JTextField sourceField;
    private JTextField destinationField;

    public ReservationFrame(DashboardFrame dashboardFrame) {
        super("Online Reservation System - Reserve Ticket");
        this.dashboardFrame = dashboardFrame;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(buildFormPanel());
    }

    private JPanel buildFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Reserve Ticket", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        int row = 0;

        nameField = new JTextField(18);
        row = addFormRow(formPanel, gbc, row, "Passenger Name:", nameField);

        ageField = new JTextField(18);
        row = addFormRow(formPanel, gbc, row, "Age:", ageField);

        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        row = addFormRow(formPanel, gbc, row, "Gender:", genderCombo);

        trainNumberField = new JTextField(18);
        row = addFormRow(formPanel, gbc, row, "Train Number:", trainNumberField);

        trainNameField = new JTextField(18);
        trainNameField.setEditable(false);
        trainNameField.setBackground(new Color(235, 235, 235));
        row = addFormRow(formPanel, gbc, row, "Train Name:", trainNameField);

        journeyDateField = new JTextField(18);
        journeyDateField.setText(DateUtil.todayAsIso());
        row = addFormRow(formPanel, gbc, row, "Journey Date (yyyy-MM-dd):", journeyDateField);

        classCombo = new JComboBox<>(new String[]{"SL", "3A", "2A", "1A", "CC", "2S"});
        row = addFormRow(formPanel, gbc, row, "Class:", classCombo);

        sourceField = new JTextField(18);
        row = addFormRow(formPanel, gbc, row, "Source:", sourceField);

        destinationField = new JTextField(18);
        row = addFormRow(formPanel, gbc, row, "Destination:", destinationField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        JButton bookButton = new JButton("Book");
        JButton resetButton = new JButton("Reset");
        JButton backButton = new JButton("Back");
        buttonPanel.add(bookButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Auto-fill train name whenever the train number field loses focus.
        trainNumberField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                autoFillTrainName();
            }
        });

        bookButton.addActionListener(e -> handleBook());
        resetButton.addActionListener(e -> resetForm());
        backButton.addActionListener(e -> {
            dispose();
            dashboardFrame.setVisible(true);
        });

        return mainPanel;
    }

    private int addFormRow(JPanel formPanel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(field, gbc);
        return row + 1;
    }

    /**
     * Looks up the train number typed by the user and auto-fills the
     * (read-only) train name field. Clears the name field and shows a
     * warning if the train number does not exist, without blocking the
     * user from continuing to edit the form.
     */
    private void autoFillTrainName() {
        String trainNumber = trainNumberField.getText().trim();
        if (trainNumber.isEmpty()) {
            trainNameField.setText("");
            return;
        }
        try {
            Train train = trainDAO.findByTrainNumber(trainNumber);
            if (train != null) {
                trainNameField.setText(train.getTrainName());
            } else {
                trainNameField.setText("");
                JOptionPane.showMessageDialog(this,
                        "No train found with number: " + trainNumber,
                        "Invalid Train Number", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error while looking up train: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBook() {
        try {
            // Ensure the train name field reflects the latest train number
            // in case the user typed it and clicked Book without tabbing away.
            autoFillTrainName();

            String name = nameField.getText().trim();
            if (!Validation.isValidName(name)) {
                throw new IllegalArgumentException("Passenger Name must contain only alphabets (2-50 characters).");
            }

            int age = Validation.parseAge(ageField.getText());

            String gender = (String) genderCombo.getSelectedItem();

            String trainNumber = trainNumberField.getText().trim();
            if (!Validation.isValidTrainNumberFormat(trainNumber)) {
                throw new IllegalArgumentException("Train Number must be a valid 5-digit number.");
            }

            Train train = trainDAO.findByTrainNumber(trainNumber);
            if (train == null) {
                throw new IllegalArgumentException("Train Number does not exist. Please enter a valid train number.");
            }
            String trainName = train.getTrainName();

            LocalDate journeyDate = Validation.parseAndValidateJourneyDate(journeyDateField.getText());

            String travelClass = (String) classCombo.getSelectedItem();

            String source = sourceField.getText().trim();
            String destination = destinationField.getText().trim();
            Validation.validateSourceDestination(source, destination);

            String pnr = reservationDAO.generateUniquePnr();
            String bookingDate = DateUtil.todayAsIso();

            Reservation reservation = new Reservation(
                    pnr, name, age, gender, trainNumber, trainName,
                    journeyDate.toString(), travelClass, source, destination, bookingDate);

            reservationDAO.save(reservation);

            JOptionPane.showMessageDialog(this,
                    "Reservation successful!\n\nYour PNR is: " + pnr
                            + "\nPlease save this PNR for viewing or cancelling your booking.",
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

            resetForm();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error while booking: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        nameField.setText("");
        ageField.setText("");
        genderCombo.setSelectedIndex(0);
        trainNumberField.setText("");
        trainNameField.setText("");
        journeyDateField.setText(DateUtil.todayAsIso());
        classCombo.setSelectedIndex(0);
        sourceField.setText("");
        destinationField.setText("");
    }
}
